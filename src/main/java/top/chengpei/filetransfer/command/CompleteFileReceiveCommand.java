package top.chengpei.filetransfer.command;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import top.chengpei.filetransfer.console.ConsoleManager;

@Data
public class CompleteFileReceiveCommand extends AbstractCommand {

    private PrepareSendFileCommand prepareSendFileCommand;

    private long elapsedTime;

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        PrepareSendFileCommand prepareSendFileCommand = this.getPrepareSendFileCommand();
        ConsoleManager.getInstance().finshRunningTask(prepareSendFileCommand.getTaskId());
    }
}
