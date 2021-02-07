package com.superkit.jt809;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.superkit.jt809.config.JT809ConfigProperties;
import com.superkit.jt809.handler.MessageDecoder;
import com.superkit.jt809.handler.NettyClientHandler;
import com.superkit.jt809.manager.ChannelManager;
import com.superkit.jt809.manager.ProcessorManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * Netty客户端 建立主链路
 *
 */
@Component
public class NettyClient implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

	@Autowired
	private JT809ConfigProperties properties;

	@Autowired
	private ProcessorManager processorManager;

	public static volatile boolean isRunning = false;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();

	@Override
	public void run(String... args) throws Exception {
		if (isRunning) {
			throw new IllegalStateException("NettyClient is already started .");
		}

		ChannelManager.mainLinkTimeOutTimes.set(0);
		ChannelManager.subLinkTimeOutTimes.set(0);

		NettyClient self = this;

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(bossGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) {
						socketChannel.pipeline().addLast(new MessageDecoder());
						socketChannel.pipeline().addLast(new NettyClientHandler(processorManager, self));
					}
				});
		// 客户端开启
		ChannelFuture channelFuture = bootstrap.connect(properties.getServerIp(), properties.getServerPort()).sync();
		isRunning = true;

		// 等待直到连接中断
		channelFuture.channel().closeFuture().sync();
	}

	/**
	 * 停止服务
	 */
	public synchronized void stopServer() {
		if (!isRunning) {
			throw new IllegalStateException("NettyClient has not started yet .");
		}
		isRunning = false;

		try {

			Future<?> future = bossGroup.shutdownGracefully().await();
			if (!future.isSuccess()) {
				log.error("bossGroup 无法正常停止:{}", future.cause());
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}

		log.info("NettyClient服务已经停止...");
	}

}
