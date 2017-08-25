package cn.asens.handler.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;
import cn.asens.process.ServerContext;

import java.io.*;

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
        if(path.equals("/")) path="index.html";
        String filePath=basePath+File.separator+path;
        File target=new File(filePath);
        log.debug(filePath);
        if(!target.exists()){
            response.sendError(HttpStatus.HTTP_404);
            response.close();
            return;
        }
        response.sendOk();
        transferTo(target,response);
    }

    private void transferTo(File target, Response response) throws IOException {
        InputStream is=new FileInputStream(target);
        BufferedInputStream bis=new BufferedInputStream(is);
        byte[] bytes=new byte[1024];
        int b;
        while((b=bis.read(bytes))!=-1){
            response.send(bytes,0,b);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        is.close();
        response.close();
    }
}
