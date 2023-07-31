package site.bleem.boot.socket.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import site.bleem.boot.socket.exchange.ReportDataDispatch;
import site.bleem.boot.socket.server.UDPServerExecutor;

/**
 * 项目启动时初始化加载数据Listener
 *
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ReportDataDispatch reportDataDispatch;

    @Autowired
    private UDPServerExecutor udpProtocolExecutor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            // 加载UDP协议处理器,并启动
            udpProtocolExecutor.setDefaultDataDispatch(reportDataDispatch);
            udpProtocolExecutor.start();
        } catch (Exception e) {
            event.getApplicationContext().close();
        }
    }
}
