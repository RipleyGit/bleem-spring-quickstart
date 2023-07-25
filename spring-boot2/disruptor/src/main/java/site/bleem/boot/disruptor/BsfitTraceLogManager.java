package site.bleem.boot.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Auther: bangsun
 * @Date: 2021/6/1 15:37
 * @Description: 数据链路监控日志管理工具
 */
public class BsfitTraceLogManager {

    private static EventFactory eventFactory = new TraceEventFactory();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；
    private static EventHandler eventHandler = new TraceEventHandler();

    private static Disruptor disruptor = new Disruptor(eventFactory,
            ringBufferSize, executor, ProducerType.SINGLE,
            new YieldingWaitStrategy());

    static {
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();
        System.out.println("------------------------Disruptor启动成功-----------------------");
    }

    public static Disruptor getDisruptor() {
        return disruptor;
    }


    /**
     * 静态内部类单例
     */
    private BsfitTraceLogManager() {
    }

    private static class BsfitTraceLogManagerHoler {
        private static BsfitTraceLogManager INSTANCE = new BsfitTraceLogManager();
    }

    public static BsfitTraceLogManager getInstance() {
        return BsfitTraceLogManagerHoler.INSTANCE;
    }





    /**
     * 记录多参数方法
     * @param traceId
     * @param eventClassName：XxxxEvent/XxxxEventObject
     * @param traceTime
     */
    public static void trace(String traceId,String eventClassName,Long traceTime) {
        Disruptor disruptor = getInstance().getDisruptor();
        RingBuffer ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            TraceaEvent event = (TraceaEvent)ringBuffer.get(sequence);//获取该序号对应的事件对象；
            event.setTraceId(traceId);
            event.setEventName(eventClassName);
            event.setTraceTime(String.valueOf(traceTime));
        } finally{
            ringBuffer.publish(sequence);//发布事件；
        }
    }

}
