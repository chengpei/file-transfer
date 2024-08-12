package top.chengpei.filetransfer.handler;

import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.chengpei.filetransfer.command.Command;

@Slf4j
public class CommandHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Command) {
            Command command = (Command) msg;
            log.info("接收到要处理的指令 {} ===> {}", command.getType(), JSONUtil.toJsonStr(command));
            command.handle(ctx);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
