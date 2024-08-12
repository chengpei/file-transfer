package top.chengpei.filetransfer.console.runnable;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import top.chengpei.filetransfer.client.ClientSession;
import top.chengpei.filetransfer.console.ConsoleManager;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public abstract class AbstractTaskRunnable implements TaskRunnable {

    private ConsoleManager consoleManager;

    public ClientSession getSession() {
        return consoleManager.getSession();
    }

    public TaskRunnable setSession(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
        return this;
    }

    @Override
    public void exec(List<String> params) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            String taskId = UUID.randomUUID().toString(true);
            consoleManager.registerRunningTask(taskId, countDownLatch);
            params.set(0, taskId);
            run(params);
            countDownLatch.await();
        } catch (Exception e) {
            log.error("执行异常", e);
        }
    }

    abstract void run(List<String> params);

}
