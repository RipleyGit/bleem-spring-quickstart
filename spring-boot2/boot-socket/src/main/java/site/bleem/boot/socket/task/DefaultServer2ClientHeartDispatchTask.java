package site.bleem.boot.socket.task;

import cn.hutool.core.codec.BCD;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.enums.ConnectProtocolType;
import site.bleem.boot.socket.enums.SDKCmdFlag;
import site.bleem.boot.socket.utils.Server2ClientSDKDataDecoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultServer2ClientHeartDispatchTask extends DefaultServer2ClientDispatchTask {
    private static final Logger log = LoggerFactory.getLogger(DefaultServer2ClientHeartDispatchTask.class);
    private static final Logger logger = LoggerFactory.getLogger("sourcelog");

    public DefaultServer2ClientHeartDispatchTask(BaseSDKData data, ConnectionEntity connectionEntity) {
        super(data, connectionEntity, false);
    }

    public void run() {
        try {
            try {
                logger.info("start Server2ClientHeartDispatchTask equHID:{} time:{}", this.connectionEntity.getEquIdentity(), super.receiveData.getTimeStr());
                BaseSDKData responseHeartData = super.receiveData.copy();
                responseHeartData.setUpTime(BCD.strToBcd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))));
                responseHeartData.setCommand((byte) SDKCmdFlag.SetResponse.getDataValue());
                responseHeartData.setState((byte)0);
                if (super.receiveData.waitack()) {
                    if (!this.connectionEntity.getChannel().isActive() || !this.connectionEntity.getChannel().isWritable()) {
                        logger.info("TCP发送失败(心跳) -> 检测到链路状态异常；address:[{}],equ:[{}] isActive:{},isWritable:{}", new Object[]{this.connectionEntity.getClientAddress(), this.connectionEntity.getEquIdentity(), this.connectionEntity.getChannel().isActive(), this.connectionEntity.getChannel().isWritable()});
                        return;
                    }

                    byte[] sendBytes = Server2ClientSDKDataDecoder.Decode(responseHeartData);
                    if (this.connectionEntity.getProtocolType() == ConnectProtocolType.Tcp) {
                        logger.info("TCP发送准备(心跳)->address:[{}],equ:[{}]", this.connectionEntity.getClientAddress(), this.connectionEntity.getEquIdentity());
                        this.connectionEntity.getChannel().writeAndFlush(Unpooled.wrappedBuffer(sendBytes)).addListener((channelFuture) -> {
                            if (channelFuture.isSuccess()) {
                                logger.info("TCP发送成功(心跳)->address:[{}] equ:[{}] 数据[{}]", new Object[]{this.connectionEntity.getClientAddress(), this.connectionEntity.getEquIdentity(), ByteBufUtil.hexDump(sendBytes)});
                            } else {
                                logger.info("TCP发送失败(心跳)->address:[{}]数据[{}] cause[{}]", new Object[]{this.connectionEntity.getClientAddress(), ByteBufUtil.hexDump(sendBytes), channelFuture.cause().getMessage()});
                            }

                        });
                    } else {
                        DatagramPacket udpSendMsg = new DatagramPacket(Unpooled.wrappedBuffer(sendBytes), new InetSocketAddress(InetAddress.getByName(this.connectionEntity.getIP()), this.connectionEntity.getPort()));
                        this.connectionEntity.getChannel().writeAndFlush(udpSendMsg).addListener((channelFuture) -> {
                            if (channelFuture.isSuccess()) {
                                logger.info("UDP发送成功(心跳)->address:[{}] equ:[{}] 数据[{}]", new Object[]{this.connectionEntity.getClientAddress(), this.connectionEntity.getEquIdentity(), ByteBufUtil.hexDump(sendBytes)});
                            } else {
                                logger.info("UDP发送失败(心跳)->address:[{}]] equ:[{}] 数据[{} cause[{}]", new Object[]{this.connectionEntity.getClientAddress(), this.connectionEntity.getEquIdentity(), ByteBufUtil.hexDump(sendBytes), channelFuture.cause().getMessage()});
                            }

                        });
                    }
                }
            } catch (Exception var4) {
                log.error("Server2ClientHeartDispatchTask error", var4);
            }

            logger.info("stop Server2ClientHeartDispatchTask equHID:{}", this.connectionEntity.getEquIdentity());
        } catch (Throwable var5) {
            throw var5;
        }
    }
}
