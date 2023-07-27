package site.bleem.boot.socket.task;

import cn.hutool.core.codec.BCD;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.container.ContainerManager;
import site.bleem.boot.socket.data.*;
import site.bleem.boot.socket.enums.SDKCmdFlag;
import site.bleem.boot.socket.utils.CRC16Rule;
import site.bleem.boot.socket.utils.DecodeRule;
import site.bleem.boot.socket.utils.Server2ClientSDKDataDecoder;
import site.bleem.boot.socket.utils.SpringUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultServer2ClientCmdDispatchUDPTask extends DefaultServer2ClientDispatchTask {
    private static final Logger log = LoggerFactory.getLogger(DefaultServer2ClientCmdDispatchUDPTask.class);
    private CmdData cmdData;
    private static final Logger logger = LoggerFactory.getLogger("sourcelog");

    public DefaultServer2ClientCmdDispatchUDPTask(CmdData cmdData, ConnectionEntity connectionEntity) {
        super((BaseSDKData)null, connectionEntity, cmdData.getResponseTimeoutSecond() > 0);
        this.cmdData = cmdData;
    }

    public void run() {
        BaseSDKData baseSDKData = this.createSDKData();
        ContainerManager manager = (ContainerManager) SpringUtil.getBean(ContainerManager.class);
        EquSendAndRequestEntity equSendAndRequestEntity = manager.getSendSeqContainer().get(this.cmdData.getEquIdentity());
        baseSDKData.setSeqence((byte)equSendAndRequestEntity.newSeq());

        byte[] cmdBytes;
        try {
            cmdBytes = Server2ClientSDKDataDecoder.Decode(baseSDKData);
        } catch (Exception var8) {
            log.error("Decode 异常", var8);
            return;
        }

        String cmdKey = String.format("%s_%s", this.cmdData.getEquIdentity(), baseSDKData.getSeqence() & 255);

        try {
            if (super.needResponse && !StringUtil.isNullOrEmpty(this.cmdData.getUuid())) {
                SendSessionEntity unfinishedCmd = new SendSessionEntity();
                unfinishedCmd.setEquIdentity(this.cmdData.getEquIdentity());
                unfinishedCmd.setSeq(baseSDKData.getSeqence() & 255);
                unfinishedCmd.setSendDataKey(cmdKey);
                unfinishedCmd.setSendUUID(this.cmdData.getUuid());
                unfinishedCmd.setSendTime(LocalDateTime.now());
                unfinishedCmd.setTimeoutSecond(this.cmdData.getResponseTimeoutSecond());
                equSendAndRequestEntity.addCmd(cmdKey, unfinishedCmd);
            }

            DatagramPacket udpSendMsg = new DatagramPacket(Unpooled.wrappedBuffer(cmdBytes), new InetSocketAddress(InetAddress.getByName(this.connectionEntity.getIP()), this.connectionEntity.getPort()));
            this.connectionEntity.getChannel().writeAndFlush(udpSendMsg).addListener((channelFuture) -> {
                if (channelFuture.isSuccess()) {
                    logger.info("UDP发送成功(命令)->address:[{}]uuid:[{}]数据:[{}]", new Object[]{this.connectionEntity.getClientAddress(), this.cmdData.getUuid(), ByteBufUtil.hexDump(cmdBytes)});
                } else {
                    logger.info("UDP发送失败(命令)->address:[{}]数据[{}] cause[{}]", new Object[]{this.connectionEntity.getClientAddress(), ByteBufUtil.hexDump(cmdBytes), channelFuture.cause().getMessage()});
                }

            });
        } catch (Exception var7) {
            log.error("下发UDP消息失败", var7);
        }

    }

    private BaseSDKData createSDKData() {
        BaseSDKData baseSDKData = new BaseSDKData();
        baseSDKData.setFrameHeader((byte)-1);
        baseSDKData.setIsEscape((byte)1);
        baseSDKData.setMainClass((byte)this.cmdData.getMainCode());
        baseSDKData.setSubClass((byte)this.cmdData.getSubCode());
        baseSDKData.setEquIdentity(DecodeRule.strLEToBcd(this.cmdData.getEquIdentity()));
        baseSDKData.setEquIdentityLen((byte)baseSDKData.getEquIdentity().length);
        baseSDKData.setUpTime(BCD.strToBcd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))));
        baseSDKData.setCommand((byte)(null == this.cmdData.getSpecialParam() ? SDKCmdFlag.Set.getDataValue() : this.cmdData.getSpecialParam().getCommandWord().getDataValue()));
        baseSDKData.setState((byte)(super.needResponse ? 64 : 0));
        baseSDKData.setInnerData(this.cmdData.getData());
        baseSDKData.setDateLen((short)baseSDKData.getInnerData().length);
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.writeShortLE(baseSDKData.getDateLen());
        compositeByteBuf.writeBytes(baseSDKData.getInnerData());
        short crc16Value = CRC16Rule.calcCrc16(compositeByteBuf, 0, baseSDKData.getDateLen() + 2);
        baseSDKData.setCrc(crc16Value);
        baseSDKData.setFrameFooter((byte)-2);
        return baseSDKData;
    }
}
