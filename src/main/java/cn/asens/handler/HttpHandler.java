package cn.asens.handler;

import cn.asens.componet.SocketChannelWrapper;
import cn.asens.handler.RequestHandler;
import cn.asens.handler.http.HttpProcessor;
import cn.asens.handler.http.HttpRequest;
import cn.asens.handler.http.HttpResponse;
import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */

public class HttpHandler implements RequestHandler{
    private static Log log= LoggerFactory.getInstance();

    @Override
    public void handle(ByteBuffer buffer, SocketChannelWrapper wrapper) {
        String message = null;
        try {
            message = new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpRequest request=new HttpRequest(message);
        request.parseHttpRequest();
        HttpResponse response=new HttpResponse(wrapper,request);
        HttpProcessor processor=new HttpProcessor();
        processor.process(request,response);
    }


}
