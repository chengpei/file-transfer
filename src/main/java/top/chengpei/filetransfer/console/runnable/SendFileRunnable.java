package top.chengpei.filetransfer.console.runnable;

import top.chengpei.filetransfer.command.PrepareSendFileCommand;

import java.io.File;
import java.util.List;

public class SendFileRunnable extends AbstractTaskRunnable {

    @Override
    public void run(List<String> params) {
        if (params.size() < 2) {
            System.err.println("send 命令参数个数不对，例如： send /path/test.txt");
            return;
        }
        String filePath = params.get(1);
        if ("hall".equals(filePath)) {
            filePath = "/Users/chengpei/IdeaProjects/wuhandsj/Intelligent-hall-management-system/dsj-smart-hall/dsj-hall-system/target/dsj-hall-system-1.0-SNAPSHOT.jar";
        } else if ("cms".equals(filePath)) {
            filePath = "/Users/chengpei/IdeaProjects/wuhandsj/dsj-enterprise-digitization-platform/dsj-ent-business/dsj-ent-cms/target/dsj-ent-cms-2.0.0.jar";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("文件不存在，" + filePath);
            return;
        }
        String path = "./";
        String fileName = filePath;
        int i = filePath.lastIndexOf("/");
        if (i > -1) {
            path = filePath.substring(0, i + 1);
            fileName = filePath.substring(i + 1);
        }
        PrepareSendFileCommand sendFileCommand = new PrepareSendFileCommand();
        sendFileCommand.setFileName(fileName);
        sendFileCommand.setSourceFilePath(path);
        sendFileCommand.setTargetFilePath("./");
        sendFileCommand.setFileSize(file.length());
        sendFileCommand.setTaskId(params.get(0));
        getSession().sendCommand(sendFileCommand);
    }
}
