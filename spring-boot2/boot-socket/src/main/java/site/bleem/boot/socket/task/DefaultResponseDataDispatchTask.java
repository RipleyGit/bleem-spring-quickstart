package site.bleem.boot.socket.task;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.container.ContainerManager;
import site.bleem.boot.socket.data.*;
import site.bleem.boot.socket.dispatch.ResponseDataDispatch;
import site.bleem.boot.socket.enums.CmdResultCode;
import site.bleem.boot.socket.utils.DecodeRule;
import site.bleem.boot.socket.utils.SpringUtil;

public class DefaultResponseDataDispatchTask extends DispatchTask {
    private static final Logger log = LoggerFactory.getLogger(DefaultResponseDataDispatchTask.class);
    protected Client2ServerResponseSDKData responseData;
    protected ConnectionEntity connectionEntity;
    protected ResponseDataDispatch reportDataDispatch;

    public DefaultResponseDataDispatchTask(BaseSDKData responseData, ConnectionEntity connectionEntity, ResponseDataDispatch reportDataDispatch) {
        this.responseData = new Client2ServerResponseSDKData(responseData);
        this.connectionEntity = connectionEntity;
        this.reportDataDispatch = reportDataDispatch;
        super.taskDispatchIdentity = this.connectionEntity.getEquIdentity().hashCode();
    }

    public void run() {
        String responseKey = String.format("%s_%s", this.connectionEntity.getEquIdentity(), DecodeRule.byteToInt(this.responseData.getBaseSDKData().getSeqence()));
        ContainerManager manager = (ContainerManager) SpringUtil.getBean(ContainerManager.class);
        SendSessionEntity unFinishedCmd = manager.getSendSeqContainer().get(this.connectionEntity.getEquIdentity()).getCmd(responseKey);
        if (unFinishedCmd != null) {
            unFinishedCmd.setHasResponse(true);
            unFinishedCmd.setResponseTime(LocalDateTime.now());
            unFinishedCmd.setResponseCode(CmdResultCode.Successed);
            CmdResultEntity responseResult = new CmdResultEntity();
            responseResult.setUuid(unFinishedCmd.getSendUUID());
            responseResult.setResponseTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            responseResult.setSendTime(unFinishedCmd.getSendTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            responseResult.setResultCode(this.responseData.responseSucceed() ? CmdResultCode.Successed.getCode() : CmdResultCode.Fail_Response_Fail.getCode());
            if (null != this.reportDataDispatch) {
                this.reportDataDispatch.handlerData(responseResult);
            }

        }
    }
}
