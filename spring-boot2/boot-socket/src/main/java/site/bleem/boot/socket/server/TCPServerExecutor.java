package site.bleem.boot.socket.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import site.bleem.boot.socket.channel.NettyTCPChannelInitializer;
import site.bleem.boot.socket.config.TcpProperties;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;
import site.bleem.boot.socket.dispatch.DefaultDataDispatch;
import site.bleem.boot.socket.utils.SpringUtil;

@Component
@ConditionalOnBean({TcpProperties.class})
public class TCPServerExecutor implements ServerExecutor {
    private static final Logger log = LoggerFactory.getLogger(TCPServerExecutor.class);
    private AtomicBoolean start = new AtomicBoolean(false);
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private AbstractDataDispatch defaultDataDispatch;
    @Autowired
    TcpProperties tcpProperties;

    public TCPServerExecutor() {
    }

    public void start() throws Exception {
        if (!this.isStarted()) {
            if (null == this.tcpProperties.getBindPorts()) {
                log.error("标准协议TCP通信组件启动失败，启动参数不正确。");
            } else {
                if (null == this.defaultDataDispatch) {
                    this.defaultDataDispatch = (AbstractDataDispatch) SpringUtil.getBean(DefaultDataDispatch.class);
                }

                this.bossGroup = new NioEventLoopGroup();
                this.workGroup = new NioEventLoopGroup();
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                ((ServerBootstrap)((ServerBootstrap)serverBootstrap.group(this.bossGroup, this.workGroup).channel(NioServerSocketChannel.class)).option(NioChannelOption.SO_REUSEADDR, true)).childOption(NioChannelOption.SO_KEEPALIVE, true).childOption(NioChannelOption.TCP_NODELAY, true).childHandler(new NettyTCPChannelInitializer(this.defaultDataDispatch));
                this.tcpProperties.getBindPorts().forEach((port) -> {
                    try {
                        serverBootstrap.bind(port).sync();
                        this.start.set(true);
                        log.info("标准协议TCP通信组件启动成功，监听端口:{}...............", port);
                    } catch (InterruptedException var4) {
                        log.error("标准协议TCP通信组件启动失败，监听端口:{}. 错误：{}", port, var4.getMessage());
                        this.start.set(false);
                        this.bossGroup.shutdownGracefully();
                        this.workGroup.shutdownGracefully();
                    }
                });
            }
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        if (this.start.get()) {
            if (this.bossGroup != null) {
                this.bossGroup.shutdownGracefully();
            }

            if (this.workGroup != null) {
                this.workGroup.shutdownGracefully();
            }

            log.info("标准协议TCP通信组件正常关闭");
        }

    }

    public boolean isStarted() {
        return this.start.get();
    }

    public AbstractDataDispatch getDefaultDataDispatch() {
        return this.defaultDataDispatch;
    }

    public void setDefaultDataDispatch(AbstractDataDispatch defaultDataDispatch) {
        this.defaultDataDispatch = defaultDataDispatch;
    }
}
