package top.chengpei.filetransfer.command;

import io.netty.channel.ChannelHandlerContext;

public interface Command {

    int getType();

    String getTaskId();

    void setTaskId(String taskId);

    void handle(ChannelHandlerContext ctx) throws Exception;
}
