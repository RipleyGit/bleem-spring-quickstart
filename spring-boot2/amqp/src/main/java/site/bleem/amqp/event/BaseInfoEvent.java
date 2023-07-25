
package site.bleem.amqp.event;


import org.springframework.context.ApplicationEvent;
import site.bleem.amqp.enums.BaseInfoEnum;
import site.bleem.amqp.source.BaseInfoSource;

public class BaseInfoEvent extends ApplicationEvent {
    private BaseInfoEnum baseInfoEnum;
    private BaseInfoSource baseInfoSource;

    public BaseInfoEvent(BaseInfoSource baseInfoSource, BaseInfoEnum baseInfoEnum) {
        super(baseInfoSource);
        this.baseInfoEnum = baseInfoEnum;
        this.baseInfoSource = baseInfoSource;
    }

    public BaseInfoEnum getBaseInfoEnum() {
        return this.baseInfoEnum;
    }

    public BaseInfoSource getBaseInfoSource() {
        return this.baseInfoSource;
    }

    public void setBaseInfoEnum(BaseInfoEnum baseInfoEnum) {
        this.baseInfoEnum = baseInfoEnum;
    }

    public void setBaseInfoSource(BaseInfoSource baseInfoSource) {
        this.baseInfoSource = baseInfoSource;
    }
}
