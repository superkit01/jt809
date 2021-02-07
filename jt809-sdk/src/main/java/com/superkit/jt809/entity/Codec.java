package com.superkit.jt809.entity;

import io.netty.buffer.ByteBuf;

public interface Codec<T> {
	/**
	 * 解码
	 * 
	 * @param buf
	 * @return
	 */
	T decode(ByteBuf buf);
	
	/**
	 * 编码
	 * 
	 * @return
	 */
	byte[] encode();
}
