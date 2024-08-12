package top.chengpei.filetransfer.command;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import top.chengpei.filetransfer.handler.FileReceiveHandler;

@Data
public class PrepareSendFileCommand extends AbstractCommand {

    private String fileName;

    private String sourceFilePath;

    private String targetFilePath;

    private long fileSize;

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        // 预发送文件指令
        ctx.pipeline().addFirst(new FileReceiveHandler(this));
        BeReadyReceiveFileCommand beReadyReceiveFileCommand = new BeReadyReceiveFileCommand();
        beReadyReceiveFileCommand.setPrepareSendFileCommand(this);
        ctx.writeAndFlush(beReadyReceiveFileCommand);
    }
}
