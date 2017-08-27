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
            whiteFile(path,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void whiteFile(String path, Response response) throws IOException {
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
        response.sendOk();
        RandomAccessFile accessFile=new RandomAccessFile(filePath,"r");
        transferTo(accessFile,response);
    }

    private void transferTo(RandomAccessFile target, Response response) throws IOException {
        response.write(target);
        response.flush();
        //response.close();
    }
}
