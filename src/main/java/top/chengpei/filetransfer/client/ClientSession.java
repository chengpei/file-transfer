package top.chengpei.filetransfer.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import top.chengpei.filetransfer.command.Command;

@Data
public class ClientSession {

    private Channel channel;

    public void sendCommand(Command command) {
        channel.writeAndFlush(command);
    }

    public void close() {
        channel.close();
    }
}
