package top.chengpei.filetransfer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import top.chengpei.filetransfer.handler.ByteToCommandDecoder;
import top.chengpei.filetransfer.handler.CommandHandler;
import top.chengpei.filetransfer.handler.CommandToByteEncoder;

public class FileTransferClient {

    private final String host;

    private final int port;

    private EventLoopGroup worker;

    private Bootstrap bootstrap;

    public FileTransferClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ClientSession connect() {
        try {
            worker = new NioEventLoopGroup();
            // 客户端启动类程序
            bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast(new LengthFieldPrepender(4));
                    pipeline.addLast(new ByteToCommandDecoder());
                    pipeline.addLast(new CommandToByteEncoder());
                    pipeline.addLast(new CommandHandler());
                }
            });
            ClientSession clientSession = new ClientSession();
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            Channel channel = channelFuture.sync().channel();
            clientSession.setChannel(channel);
            return clientSession;
        } catch (Exception ex) {
            throw new RuntimeException("Starting socket client failed", ex);
        }
    }

    public void stop() {
        worker.shutdownGracefully();
    }
}
