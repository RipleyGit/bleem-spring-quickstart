package site.bleem.boot.socket.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PreDestroy;

import cn.hutool.core.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskDispatcher {
    private static final Logger log = LoggerFactory.getLogger(TaskDispatcher.class);
    private final int CORE_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private ExecutorService[] executorsUp;
    private ExecutorService[] executorsDown;

    public TaskDispatcher() {
        this.executorsUp = new ExecutorService[this.CORE_SIZE / 2 == 0 ? 1 : this.CORE_SIZE / 2 + this.CORE_SIZE % 2];
        this.executorsDown = new ExecutorService[this.CORE_SIZE / 2 == 0 ? 1 : this.CORE_SIZE / 2];

        int i;
        ExecutorService executor;
        for(i = 0; i < this.executorsUp.length; ++i) {
            executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("message-dispatcher-up-executors-" + i,false));
            this.executorsUp[i] = executor;
        }

        for(i = 0; i < this.executorsDown.length; ++i) {
            executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("message-dispatcher-down-executors-" + i,false));
            this.executorsDown[i] = executor;
        }

        log.info("Runtime.getRuntime().availableProcessors() {}", Runtime.getRuntime().availableProcessors());
        log.info("executorsUp size: {}", this.executorsUp.length);
        log.info("executorsDown size: {}", this.executorsDown.length);
    }

    public void addNewTask(DispatchTask task, int type) {
        try {
            int index;
            if (type == 0) {
                index = task.getTaskDispatchIdentity() % this.executorsUp.length;
                this.executorsUp[Math.abs(index)].submit(task);
            } else if (type == 1) {
                index = task.getTaskDispatchIdentity() % this.executorsDown.length;
                this.executorsDown[Math.abs(index)].submit(task);
            }
        } catch (Exception var4) {
            log.info("addNewTask", var4);
        }

    }

    @PreDestroy
    public void dispose() {
        ExecutorService[] var1 = this.executorsUp;
        int var2 = var1.length;

        int var3;
        ExecutorService innerService;
        for(var3 = 0; var3 < var2; ++var3) {
            innerService = var1[var3];
            innerService.shutdownNow();
        }

        var1 = this.executorsDown;
        var2 = var1.length;

        for(var3 = 0; var3 < var2; ++var3) {
            innerService = var1[var3];
            innerService.shutdownNow();
        }

    }
}
