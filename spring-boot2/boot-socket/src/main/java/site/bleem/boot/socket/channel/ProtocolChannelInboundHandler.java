package site.bleem.boot.socket.channel;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.lang.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.container.ContainerManager;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.CmdData;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.data.OriMessageEntity;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;
import site.bleem.boot.socket.enums.ConnectProtocolType;
import site.bleem.boot.socket.enums.SDKCmdFlag;
import site.bleem.boot.socket.task.DefaultServer2ClientCmdDispatchTCPTask;
import site.bleem.boot.socket.task.DefaultServer2ClientCmdDispatchUDPTask;
import site.bleem.boot.socket.task.DispatchTask;
import site.bleem.boot.socket.task.TaskDispatcher;
import site.bleem.boot.socket.utils.Client2ServerSDKDataEncoder;
import site.bleem.boot.socket.utils.DecodeRule;
import site.bleem.boot.socket.utils.SpringUtil;

public class ProtocolChannelInboundHandler extends SimpleChannelInboundHandler<OriMessageEntity> {
    private static final Logger log = LoggerFactory.getLogger(ProtocolChannelInboundHandler.class);
    private TaskDispatcher taskDispatcher = (TaskDispatcher) SpringUtil.getBean(TaskDispatcher.class);
    private ContainerManager containerManager = (ContainerManager)SpringUtil.getBean(ContainerManager.class);
    private AbstractDataDispatch defaultDispatch;

    public ProtocolChannelInboundHandler(AbstractDataDispatch defaultDispatch) {
        this.defaultDispatch = defaultDispatch;
    }

    protected void channelRead0(ChannelHandlerContext ctx, OriMessageEntity msg) {
        BaseSDKData receivedData = Client2ServerSDKDataEncoder.encode(msg.getOriData());
        if (receivedData != null) {
            ConnectionEntity connectionEntity = this.containerManager.getNettyRpcClientContainer().add(receivedData.getEquIdentityStr(), ctx.channel(), msg.getProtocolType(), msg.getAddress(), msg.getPort());
            if (connectionEntity == null) {
                log.info("未找到设备[{}]对应的链路信息", receivedData.getEquIdentityStr());
            } else {
                if (receivedData.getCommand() == SDKCmdFlag.HeartBeat.getDataValue()) {
                    if (connectionEntity.getHeartBeatCount() == 0) {
                        log.info("设备[{}]首次心跳连接,对设备下发校时命令", receivedData.getEquIdentityStr());
                        this.addTimingTask(connectionEntity);
                    } else {
                        LocalDateTime reportDateTime = LocalDateTime.parse(receivedData.getTimeStr(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                        if (Math.abs(Duration.between(reportDateTime, LocalDateTime.now()).getSeconds()) > 30L) {
                            log.info("设备[{}]上报时间{}与系统时间偏差超过{},对设备下发校时命令", new Object[]{receivedData.getEquIdentityStr(), receivedData.getTimeStr(), 60});
                            this.addTimingTask(connectionEntity);
                        }
                    }

                    connectionEntity.setLastHeartBeatTime(LocalDateTime.now());
                    this.defaultDispatch.dispatchHeartData(receivedData, connectionEntity);
                } else if (receivedData.getCommand() == SDKCmdFlag.SetResponse.getDataValue()) {
                    this.defaultDispatch.dispatchResponseData(receivedData, connectionEntity);
                } else if (receivedData.getCommand() == SDKCmdFlag.Report.getDataValue() || receivedData.getCommand() == SDKCmdFlag.ReadResponse.getDataValue()) {
                    this.defaultDispatch.dispatchReportData(receivedData, connectionEntity);
                }

            }
        }
    }

    private void addTimingTask(ConnectionEntity connectionEntity) {
        CmdData timingCmdData = new CmdData();
        timingCmdData.setMainCode(224);
        timingCmdData.setSubCode(1);
        timingCmdData.setResponseTimeoutSecond(3);
        timingCmdData.setUuid(UUID.randomUUID().toString());
        timingCmdData.setEquIdentity(connectionEntity.getEquIdentity());
        ByteBuf timingBuf = Unpooled.buffer();
        byte[] hidBytes = DecodeRule.strLEToBcd(connectionEntity.getEquIdentity());
        timingBuf.writeIntLE(66);
        timingBuf.writeByte(1);
        timingBuf.writeByte((byte)hidBytes.length);
        timingBuf.writeBytes(hidBytes);
        timingBuf.writeBytes(BCD.strToBcd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))));
        timingBuf.writeByte((byte)LocalDateTime.now().getDayOfWeek().getValue());
        byte[] bytes = new byte[timingBuf.readableBytes()];
        timingBuf.readBytes(bytes);
        timingCmdData.setData(bytes);
        timingBuf.release();
        Object dispatchTask;
        if (connectionEntity.getProtocolType() == ConnectProtocolType.Tcp) {
            dispatchTask = new DefaultServer2ClientCmdDispatchTCPTask(timingCmdData, connectionEntity, 0);
        } else {
            dispatchTask = new DefaultServer2ClientCmdDispatchUDPTask(timingCmdData, connectionEntity);
        }

        this.taskDispatcher.addNewTask((DispatchTask)dispatchTask, 1);
    }
}
