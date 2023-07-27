package site.bleem.amqp.listener;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import site.bleem.amqp.enums.BaseInfoEnum;
import site.bleem.amqp.event.BaseInfoEvent;
import site.bleem.amqp.source.BaseInfoSource;
import site.bleem.amqp.util.RabbitmqUtil;


@Component
public class BaseInfoEventListener implements ApplicationListener<BaseInfoEvent> {
    private static final Logger log = LoggerFactory.getLogger(BaseInfoEventListener.class);
    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    public BaseInfoEventListener() {
    }

    public void onApplicationEvent(BaseInfoEvent baseInfoEvent) {
        log.info("处理基础信息事件信息：{}", JSON.toJSONString(baseInfoEvent));
        BaseInfoSource baseInfoSource = baseInfoEvent.getBaseInfoSource();
        if (ObjectUtil.isNull(baseInfoSource)) {
            log.info("没有事件信息，暂不处理：{}", JSON.toJSONString(baseInfoSource));
        } else {
            if (baseInfoEvent.getBaseInfoEnum() == BaseInfoEnum.AREA) {
                this.rabbitmqUtil.sendMsgToExchange(baseInfoSource.getExchange(), baseInfoSource.getRoutingKey(), baseInfoSource.getMsg());
            } else {
                log.info("暂无此事件：{}", JSON.toJSONString(baseInfoSource));
            }

        }
    }
}
