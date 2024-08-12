package top.chengpei.filetransfer.console.runnable;

import top.chengpei.filetransfer.command.AcquireFileRequestCommand;

import java.util.List;

public class DownloadFileRunnable extends AbstractTaskRunnable {

    @Override
    public void run(List<String> params) {
        if (params.size() < 2) {
            System.err.println("get 命令参数个数不对，例如： get /path/test.txt");
            return;
        }
        String filePath = params.get(1);
        String path = "./";
        String fileName = filePath;
        int i = filePath.lastIndexOf("/");
        if (i > -1) {
            path = filePath.substring(0, i + 1);
            fileName = filePath.substring(i + 1);
        }
        AcquireFileRequestCommand sendFileCommand = new AcquireFileRequestCommand();
        sendFileCommand.setFileName(fileName);
        sendFileCommand.setFilePath(path);
        sendFileCommand.setTaskId(params.get(0));
        getSession().sendCommand(sendFileCommand);
    }
}
