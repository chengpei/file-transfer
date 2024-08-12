package top.chengpei.filetransfer.handler;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import top.chengpei.filetransfer.command.Command;
import top.chengpei.filetransfer.utils.CommandUtils;

import java.util.List;

@Slf4j
public class ByteToCommandDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        Class<? extends Command> commandType = CommandUtils.getCommandType(byteBuf.readInt());
        int readableBytes = byteBuf.readableBytes();
        byte[] bytes = new byte[readableBytes];
        byteBuf.readBytes(bytes);
        Command command = JSONUtil.toBean(new String(bytes), commandType);
        list.add(command);
    }
}
