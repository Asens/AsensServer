package cn.asens.handler.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;
import cn.asens.process.ServerContext;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asens on 2017/8/25
 */
public class HttpProcessor implements Processor{
    private static Log log= LoggerFactory.getInstance();

    @Override
    public void process(Request request, Response response) {
        String path=request.getRequestPath();
        try {
            whiteFile(path,response,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void whiteFile(String path, Response response, Request request) throws IOException {
        String basePath= ServerContext.ROOT_PATH+"index";
        if(path.equals("/")) path="/index.html";
        String filePath=basePath+path;


        File target=new File(filePath);
        log.debug(filePath);
        if(!target.exists()){
            response.sendError(HttpStatus.HTTP_404);
            response.close();
            return;
        }
        RandomAccessFile accessFile=new RandomAccessFile(filePath,"r");
        response.sendOk(accessFile.length());

        if(request.getAccept().contains("text/css")){
            response.setContentType("text/css");
        }
        response.setHeaderEnd();

        transferTo(accessFile,response);
    }

    private void transferTo(RandomAccessFile target, Response response) throws IOException {
        response.write(target);
        response.flush();
        //response.close();
    }
}
