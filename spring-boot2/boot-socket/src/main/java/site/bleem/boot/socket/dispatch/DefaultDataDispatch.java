package site.bleem.boot.socket.dispatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.enums.DispatchTaskType;
import site.bleem.boot.socket.task.*;

@Component
public class DefaultDataDispatch extends AbstractDataDispatch {
    @Autowired
    TaskDispatcher taskDispatcher;

    public DefaultDataDispatch() {
    }

    public void dispatchHeartData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
        if (receivedData.waitack()) {
            DefaultServer2ClientHeartDispatchTask downTask = new DefaultServer2ClientHeartDispatchTask(receivedData, connectionEntity);
            this.addTask(this.taskDispatcher, downTask, DispatchTaskType.Down.getCode());
            DefaultClient2ServerDispatchTask upTask = new DefaultClient2ServerDispatchTask(receivedData, connectionEntity);
            this.addTask(this.taskDispatcher, upTask, DispatchTaskType.Up.getCode());
        } else {
            DefaultClient2ServerDispatchTask task = new DefaultClient2ServerDispatchTask(receivedData, connectionEntity);
            this.addTask(this.taskDispatcher, task, DispatchTaskType.Up.getCode());
        }

    }

    public void dispatchResponseData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
        DefaultResponseDataDispatchTask task = new DefaultResponseDataDispatchTask(receivedData, connectionEntity, (ResponseDataDispatch)null);
        this.addTask(this.taskDispatcher, task, DispatchTaskType.Up.getCode());
    }

    public void dispatchReportData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
        DefaultClient2ServerDispatchTask task = new DefaultClient2ServerDispatchTask(receivedData, connectionEntity);
        this.addTask(this.taskDispatcher, task, DispatchTaskType.Up.getCode());
    }

    public void addTask(TaskDispatcher taskDispatcher, DispatchTask dispatchTask, int type) {
        taskDispatcher.addNewTask(dispatchTask, type);
    }
}
