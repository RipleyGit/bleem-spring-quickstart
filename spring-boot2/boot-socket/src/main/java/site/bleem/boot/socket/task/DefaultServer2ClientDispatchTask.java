package site.bleem.boot.socket.task;

import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;

public abstract class DefaultServer2ClientDispatchTask extends DispatchTask {
    protected int taskSource;
    protected BaseSDKData receiveData;
    protected ConnectionEntity connectionEntity;
    protected boolean needResponse;

    public DefaultServer2ClientDispatchTask(ConnectionEntity connectionEntity, boolean needResponse) {
        this((BaseSDKData)null, connectionEntity, needResponse);
    }

    public DefaultServer2ClientDispatchTask(BaseSDKData data, ConnectionEntity connectionEntity, boolean needResponse) {
        this.taskSource = 0;
        this.receiveData = data;
        this.connectionEntity = connectionEntity;
        this.needResponse = needResponse;
        super.taskDispatchIdentity = this.connectionEntity.getEquIdentity().hashCode();
    }
}
