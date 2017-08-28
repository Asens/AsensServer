package cn.asens.process;

import cn.asens.componet.SocketChannelWrapper;
import cn.asens.handler.DefaultHandler;
import cn.asens.handler.RequestHandler;
import cn.asens.handler.HttpHandler;
import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Asens
 * create 2017-08-23 20:36
 **/

public class ServerContext {
    private final static RequestHandler handler=new HttpHandler();
    public final static String ROOT_PATH=rootPath();
    private static Log log= LoggerFactory.getInstance();

    private static String rootPath() {
        URL url=Thread.currentThread().getContextClassLoader().getResource("");
        if(url==null){
            log.error("root path not found");
            throw new RuntimeException("root path not found");
        }
        return url.getFile();
    }

    public static void fireMessageReceived(ByteBuffer buffer, SocketChannelWrapper channelWrapper){
        handler.handle(buffer,channelWrapper);
    }
}
