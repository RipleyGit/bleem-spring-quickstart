package site.bleem.boot.socket.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import site.bleem.boot.socket.channel.NettyUDPChannelInitializer;
import site.bleem.boot.socket.config.UdpProperties;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;
import site.bleem.boot.socket.dispatch.DefaultDataDispatch;
import site.bleem.boot.socket.utils.SpringUtil;

@Component
@ConditionalOnBean({UdpProperties.class})
public class UDPServerExecutor implements ServerExecutor {
    private static final Logger log = LoggerFactory.getLogger(UDPServerExecutor.class);
    private AtomicBoolean start = new AtomicBoolean(false);
    private EventLoopGroup bossGroup;
    private AbstractDataDispatch defaultDataDispatch;
    @Autowired
    UdpProperties udpProperties;

    public UDPServerExecutor() {
    }

    public void start() throws Exception {
        if (!this.isStarted()) {
            if (null == this.udpProperties.getBindPorts()) {
                log.error("标准协议UDP通信组件启动失败，启动参数不正确。");
            } else {
                if (null == this.defaultDataDispatch) {
                    this.defaultDataDispatch = (AbstractDataDispatch) SpringUtil.getBean(DefaultDataDispatch.class);
                }

                this.bossGroup = new NioEventLoopGroup();
                Bootstrap serverBootstrap = new Bootstrap();
                ((Bootstrap)((Bootstrap)((Bootstrap)serverBootstrap.group(this.bossGroup)).channel(NioDatagramChannel.class)).option(ChannelOption.SO_BROADCAST, true)).handler(new NettyUDPChannelInitializer(this.defaultDataDispatch));
                this.udpProperties.getBindPorts().forEach((port) -> {
                    try {
                        ChannelFuture udpFuture = serverBootstrap.bind(port).sync();
                        udpFuture.channel().closeFuture().addListener((future) -> {
                            log.info("udpChannel close");
//                          todo  future.channel().close();
                        });
                        log.info("Netty UDP服务启动成功，监听端口:{}...............", port);
                    } catch (Exception var4) {
                        log.error("Netty UDP服务启动失败，监听端口:{}. 错误：{}", port, var4.getMessage());
                        this.start.set(false);
                        this.bossGroup.shutdownGracefully();
                    }
                });
            }
        }
    }

    public void stop() throws Exception {
        if (this.start.get()) {
            if (this.bossGroup != null) {
                this.bossGroup.shutdownGracefully();
            }

            log.info("标准协议UDP通信组件正常关闭");
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
