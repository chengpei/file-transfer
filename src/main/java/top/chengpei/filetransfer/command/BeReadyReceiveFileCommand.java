package top.chengpei.filetransfer.command;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;

@Data
public class BeReadyReceiveFileCommand extends AbstractCommand {

    private PrepareSendFileCommand prepareSendFileCommand;

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) throws Exception {
        // 发送文件指令
        PrepareSendFileCommand prepareSendFileCommand = this.getPrepareSendFileCommand();
        File file = new File(prepareSendFileCommand.getSourceFilePath() + prepareSendFileCommand.getFileName());
        FileInputStream fileInputStream = new FileInputStream(file);
        DefaultFileRegion fileRegion = new DefaultFileRegion(fileInputStream.getChannel(), 0, file.length());
        ctx.writeAndFlush(fileRegion).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("文件发送完成！！！");
            } else {
                System.err.println("文件发送失败。。。");
            }
            fileInputStream.close();
        });
    }
}
