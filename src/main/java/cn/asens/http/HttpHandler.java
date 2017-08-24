package cn.asens.http;

import cn.asens.handler.RequestHandler;
import cn.asens.process.ServerContext;

import java.io.File;
import java.io.IOException;
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
        request.parseHttpRequest();
        String path=request.getRequestPath();
        try {
            whiteFile(path,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void whiteFile(String path, HttpResponse response) throws IOException {
        String basePath=ServerContext.ROOT_PATH+"index";
        if(path.equals("/")) path="index.html";
        String filePath=basePath+File.separator+path;
        File target=new File(filePath);
        if(!target.exists()){
            response.sendError(404);
            response.close();
            return;
        }

        transferTo(target,response);
    }

    private void transferTo(File target, HttpResponse response) {

    }
}
