package com.superkit.jt809;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.superkit.jt809.config.JT809ConfigProperties;
import com.superkit.jt809.handler.MessageDecoder;
import com.superkit.jt809.handler.NettyServerHandler;
import com.superkit.jt809.manager.ProcessorManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * 
 * Netty服务端 建立从链路
 *
 */
@Component
public class NettyServer implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

	@Autowired
	private JT809ConfigProperties properties;

	@Autowired
	private ProcessorManager processorManager;
	
	public static volatile boolean isRunning = false;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();


	@Override
	public void run(String... args) throws Exception {
		if (isRunning) {
			throw new IllegalStateException("NettyServer is already started .");
		}

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 65535).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel socketChannel) {
						socketChannel.pipeline().addLast(new MessageDecoder());
						socketChannel.pipeline().addLast(new NettyServerHandler(processorManager));
					}
				});

		// 等待客户端连接成功
		ChannelFuture channelFuture = serverBootstrap.bind(properties.getLocalPort()).sync();
		isRunning = true;

		// 等待客户端断开
		channelFuture.channel().closeFuture().sync();

	}

	/**
	 * 停止服务
	 */
	public synchronized void stopServer() {
		if (!isRunning) {
			throw new IllegalStateException("NettyServer has not started yet.");
		}
		isRunning = false;

		try {
			Future<?> future = this.workerGroup.shutdownGracefully().await();
			if (!future.isSuccess()) {
				log.error("workerGroup 无法正常停止:{}", future.cause());
			}

			future = this.bossGroup.shutdownGracefully().await();
			if (!future.isSuccess()) {
				log.error("bossGroup 无法正常停止:{}", future.cause());
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
		log.info("NettyServer服务已经停止...");
	}

}
