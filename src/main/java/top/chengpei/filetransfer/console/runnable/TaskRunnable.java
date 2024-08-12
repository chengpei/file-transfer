package top.chengpei.filetransfer.console.runnable;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface TaskRunnable {

    void exec(List<String> params);
}
