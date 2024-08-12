package top.chengpei.filetransfer.command;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.io.File;

@Data
public class AcquireFileRequestCommand extends AbstractCommand {

    private String fileName;

    private String filePath;

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        // 拉取文件
        PrepareSendFileCommand sendFileCommand = new PrepareSendFileCommand();
        File file = new File(this.getFilePath() + this.getFileName());
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        sendFileCommand.setFileName(this.fileName);
        sendFileCommand.setSourceFilePath(this.filePath);
        sendFileCommand.setTargetFilePath("./");
        sendFileCommand.setFileSize(file.length());
        sendFileCommand.setTaskId(this.getTaskId());
        ctx.writeAndFlush(sendFileCommand);
    }
}
