package cn.asens.process;

import cn.asens.handler.DefaultHandler;
import cn.asens.handler.RequestHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Asens
 * create 2017-08-23 20:36
 **/

public class ServerContext {
    private final static RequestHandler handler=new DefaultHandler();

    public static void fireMessageReceived(ByteBuffer buffer, SocketChannel channel){
        handler.handle(buffer,channel);
    }
}
