package top.chengpei.filetransfer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import top.chengpei.filetransfer.handler.ByteToCommandDecoder;
import top.chengpei.filetransfer.handler.CommandHandler;
import top.chengpei.filetransfer.handler.CommandToByteEncoder;

public class FileTransferServer {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap serverBootstrap;

    private ChannelFuture channelFuture;

    public FileTransferServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast(new LengthFieldPrepender(4));
                        pipeline.addLast(new ByteToCommandDecoder());
                        pipeline.addLast(new CommandToByteEncoder());
                        pipeline.addLast(new CommandHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public void start(int port) {
        try {
            channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex) {
            throw new RuntimeException("Starting socket server failed", ex);
        } finally {
            stop();
        }
    }

    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        if (channelFuture != null) {
            channelFuture.channel().close();
        }
    }
}
