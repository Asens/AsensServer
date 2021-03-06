package cn.asens.handler;

import cn.asens.componet.SocketChannelWrapper;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Asens
 * create 2017-08-23 20:30
 **/

public interface RequestHandler {
    void handle(ByteBuffer buffer, SocketChannelWrapper wrapper);
}
