package com.superkit.jt809.handler;

import java.util.List;

import com.superkit.jt809.MsgConstant;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        in.markReaderIndex();
        byte flag = in.readByte();
        if (flag == MsgConstant.HEAD_FLAG) {
            int readableLen = in.readableBytes();
            int nextFlag = in.readSlice(readableLen).indexOf(0, readableLen, MsgConstant.TAIL_FLAG);
            if (nextFlag != -1) {
                in.resetReaderIndex();
                list.add(in.readSlice(nextFlag + 2).retain());
            } else {
                in.resetReaderIndex();
            }
        } else {
            //数据错误，主动断开
            channelHandlerContext.channel().close();
        }
    }
}

