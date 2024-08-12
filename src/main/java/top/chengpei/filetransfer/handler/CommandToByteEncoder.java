package top.chengpei.filetransfer.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import top.chengpei.filetransfer.command.Command;
import top.chengpei.filetransfer.utils.CommandUtils;

@Slf4j
public class CommandToByteEncoder extends MessageToByteEncoder<Command> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf byteBuf) {
        byteBuf.writeInt(command.getType());
        byteBuf.writeBytes(CommandUtils.encode(command).getBytes());
    }
}
