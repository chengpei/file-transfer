package top.chengpei.filetransfer.handler;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.chengpei.filetransfer.command.CompleteFileReceiveCommand;
import top.chengpei.filetransfer.command.PrepareSendFileCommand;
import top.chengpei.filetransfer.console.ConsoleManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

@Slf4j
@Data
public class FileReceiveHandler extends ChannelInboundHandlerAdapter {

    private PrepareSendFileCommand prepareSendFileCommand;

    private File file;

    private FileOutputStream fos;

    private long count = 0;

    private long totalSize = 0;

    private long lastSize = 0;

    private Thread showProgressThread;

    private boolean sending = false;

    private long startTime;

    public FileReceiveHandler(PrepareSendFileCommand prepareSendFileCommand) {
        try {
            this.prepareSendFileCommand = prepareSendFileCommand;
            file = new File(prepareSendFileCommand.getTargetFilePath() + prepareSendFileCommand.getFileName());
            fos = new FileOutputStream(file);
            showProgressThread = new Thread(() -> {
                while (sending) {
                    ThreadUtil.sleep(1000);
                    if (count > 0) {
                        long addSize = totalSize - lastSize;
                        log.info("当前接收包{}个，接收字节{}, 传输速度：{}/s", count, totalSize, getNetFileSizeDescription(addSize));
                        lastSize = totalSize;
                    }
                }
            });
        } catch (Exception e) {
            log.error("FileReceiveHandler", e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!showProgressThread.isAlive()) {
            sending = true;
            startTime = System.currentTimeMillis();
            showProgressThread.start();
        }
        ByteBuf byteBuf = (ByteBuf) msg;
        int readableBytes = byteBuf.readableBytes();
        byte[] bytes = new byte[readableBytes];
        byteBuf.readBytes(bytes);
        fos.write(bytes);
        byteBuf.release();
        totalSize += readableBytes;
        count++;
        if (totalSize == prepareSendFileCommand.getFileSize()) {
            if (fos != null) {
                fos.close();
                sending = false;
                log.info("传输结束，耗时: {}，平均速度：{}/s", getTimeStr(System.currentTimeMillis() - startTime), getNetFileSizeDescription(totalSize / (System.currentTimeMillis() - startTime) / 1000));
            }
            ctx.pipeline().remove(this);
            CompleteFileReceiveCommand completeFileReceiveCommand = new CompleteFileReceiveCommand();
            completeFileReceiveCommand.setPrepareSendFileCommand(prepareSendFileCommand);
            completeFileReceiveCommand.setElapsedTime(System.currentTimeMillis() - startTime);
            ctx.pipeline().writeAndFlush(completeFileReceiveCommand);
            ConsoleManager.getInstance().finshRunningTask(prepareSendFileCommand.getTaskId());
        }
    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    public static String getTimeStr(long size) {
        long s = size / 1000;
        long y = s % 60;
        long m = s / 60;
        if (m == 0) {
            return y + "秒";
        }
        return m + "分" + y + "秒";
    }
}
