package site.bleem.boot.socket.exchange;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;
import site.bleem.boot.socket.enums.DispatchTaskType;
import site.bleem.boot.socket.task.DefaultServer2ClientHeartDispatchTask;
import site.bleem.boot.socket.task.TaskDispatcher;

/**
 * 设备上报数据解析后接收处理器
 * @author admin
 * @version v1.0
 * @date 2022/3/11 14:09
 */
@Component
public class ReportDataDispatch extends AbstractDataDispatch {

    @Autowired
    private TaskDispatcher taskDispatcher;
//    @Autowired
//    private ProtocolConfigCache protocolConfigCache;
//    @Autowired
//    private HeartingDispatchTaskHandle heartingDispatchTaskHandle;
//    @Autowired
//    private ReportDispatchTaskHandle reportDispatchTaskHandle;
//    @Autowired
//    private ResponseDispatchTaskHandle responseDispatchTaskHandle;

    /**
     * 接收设备心跳数据
     * @param receivedData
     * @param connectionEntity
     * @author xu_xl
     * @date 2021-3-11 14:09:00
     */
    @Override
    public void dispatchHeartData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
//        // 判断是否需要回复心跳帧
//        if (receivedData.waitack()){
//            //下发心跳,收到数据对应下发
//            DefaultServer2ClientHeartDispatchTask downTask = new DefaultServer2ClientHeartDispatchTask(receivedData, connectionEntity);
//            taskDispatcher.addNewTask(downTask, DispatchTaskType.Down.getCode());
//        }
//        HeartingDataDispatchTask upTask = new HeartingDataDispatchTask(receivedData,connectionEntity,protocolConfigCache,heartingDispatchTaskHandle);
//        taskDispatcher.addNewTask(upTask,DispatchTaskType.Up.getCode());
    }

    /**
     * 接收设备下发指令后返回的响应数据
     * @param receivedData
     * @param connectionEntity
     * @author xu_xl
     * @date 2021-3-11 14:09:00
     */
    @Override
    public void dispatchResponseData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
//        ResponseDataDispatchTask responseTask2 = new ResponseDataDispatchTask(receivedData,connectionEntity,responseDispatchTaskHandle);
//        taskDispatcher.addNewTask(responseTask2,DispatchTaskType.Up.getCode());
    }

    /**
     * 接收设备主动上报的数据
     *    如：设备语音状态、运行状态、ip、sim、音量、上行调度对讲等
     * @param receivedData
     * @param connectionEntity
     * @author xu_xl
     * @date 2021-3-11 14:09:00
     */
    @Override
    public void dispatchReportData(BaseSDKData receivedData, ConnectionEntity connectionEntity) {
//        ReportDataDispatchTask upTask = new ReportDataDispatchTask(receivedData,connectionEntity,protocolConfigCache,reportDispatchTaskHandle);
//        taskDispatcher.addNewTask(upTask,DispatchTaskType.Up.getCode());
    }
}
