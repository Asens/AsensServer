package cn.asens.http;

import cn.asens.handler.RequestHandler;
import cn.asens.process.ServerContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */

public class HttpHandler implements RequestHandler{
    @Override
    public void handle(ByteBuffer buffer, SocketChannel channel) {
        String message = null;
        try {
            message = new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpRequest request=new HttpRequest(message);
        HttpResponse response=new HttpResponse(channel);
        String path=request.getRequestPath();
        whiteFile(path,response);

    }

    private void whiteFile(String path, HttpResponse response) {

    }
}
