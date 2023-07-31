package site.bleem.boot.socket.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;

public class DefaultClient2ServerDispatchTask extends DispatchTask {
    private static final Logger log = LoggerFactory.getLogger(DefaultClient2ServerDispatchTask.class);
    protected BaseSDKData baseSDKData;
    protected ConnectionEntity connectionEntity;

    public DefaultClient2ServerDispatchTask(BaseSDKData data, ConnectionEntity connectionEntity) {
        this.baseSDKData = data;
        this.connectionEntity = connectionEntity;
        super.taskDispatchIdentity = this.connectionEntity.getEquIdentity().hashCode();
    }

    public void run() {
        log.info("not support");
    }
}
