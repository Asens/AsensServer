package cn.asens.http;

import cn.asens.handler.RequestHandler;
import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;
import cn.asens.process.ServerContext;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */

public class HttpHandler implements RequestHandler{
    private static Log log= LoggerFactory.getInstance();

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
        log.debug(filePath);
        if(!target.exists()){
            response.sendError(404);
            response.close();
            return;
        }
        response.sendOk();
        transferTo(target,response);
    }

    private void transferTo(File target, HttpResponse response) throws IOException {
        InputStream is=new FileInputStream(target);
        BufferedInputStream bis=new BufferedInputStream(is);
        byte[] bytes=new byte[1024];
        int b;
        while((b=bis.read(bytes))!=-1){
            response.send(bytes,0,b);
        }
        response.close();
    }
}
