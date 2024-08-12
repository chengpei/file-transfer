package top.chengpei.filetransfer.console;

import lombok.Data;
import top.chengpei.filetransfer.client.ClientSession;
import top.chengpei.filetransfer.console.runnable.DownloadFileRunnable;
import top.chengpei.filetransfer.console.runnable.SendFileRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Data
public class ConsoleManager {

    private ClientSession session;

    private ThreadPoolExecutor threadPoolExecutor;

    private Map<String, ConsoleTask> consoleTaskMap = new HashMap<>();

    private Map<String, CountDownLatch> runningTaskMap = new HashMap<>();

    private static ConsoleManager instance;

    public static synchronized ConsoleManager init(ClientSession session) {
        if (instance == null) {
            instance = new ConsoleManager(session);
        } else {
            throw new RuntimeException("已经初始化过了");
        }
        return instance;
    }

    public static ConsoleManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("还未初始化");
        }
        return instance;
    }

    private ConsoleManager(ClientSession session) {
        this.session = session;
        consoleTaskMap.put("send", new ConsoleTask("send", new SendFileRunnable().setSession(this)));
        consoleTaskMap.put("get", new ConsoleTask("get", new DownloadFileRunnable().setSession(this)));
    }

    public void registerRunningTask(String taskId, CountDownLatch countDownLatch) {
        runningTaskMap.put(taskId, countDownLatch);
    }

    public void finshRunningTask(String taskId) {
        CountDownLatch countDownLatch = runningTaskMap.get(taskId);
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void exec(String cmd) {
        if (session == null) {
            System.err.println("服务端只支持stop");
        }
        if (cmd == null) {
            return;
        }
        cmd = cmd.trim();
        if (cmd.isEmpty()) {
            return;
        }
        List<String> params = Arrays.stream(cmd.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        ConsoleTask consoleTask = consoleTaskMap.get(params.get(0));
        if (consoleTask == null) {
            System.err.println("未知的指令 " + params.get(0));
            return;
        }
        consoleTask.getRunnable().exec(params);
    }
}
