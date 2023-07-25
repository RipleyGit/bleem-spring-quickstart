package site.bleem.boot.disruptor;

import com.lmax.disruptor.EventFactory;

public class TraceEventFactory implements EventFactory {
    public TraceaEvent newInstance()
    {
        return new TraceaEvent();
    }
}
