package com.rick.socket.netty.protocol.auth;

import com.rick.socket.netty.protocol.entry.Header;
import com.rick.socket.netty.protocol.entry.MessageType;
import com.rick.socket.netty.protocol.entry.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 握手登录验证
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message != null && message.getHeader() != null &&
                message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte body = (byte) message.getBody();
            if (body != 0) {
                System.out.println("握手失败");
                ctx.close();
            } else {
                System.out.println("握手成功：" + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            // ??????????
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //?????????
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }
}
