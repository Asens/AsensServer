package cn.asens.handler;

import cn.asens.componet.*;
import cn.asens.handler.http.HttpRequest;
import cn.asens.util.HttpUtils;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asens on 2017/8/30
 */
public class ProxyHandler implements RequestHandler{

    @Override
    public void handle(ByteBuffer buffer, SocketChannelWrapper wrapper) {
        String message = null;
        try {
            message = new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(message!=null&&message.contains("favicon.ico")) return;
        HttpRequest request = new HttpRequest(message);
        request.parseHttpRequest();

        try {
            InputStream is=getHttpBack(request,wrapper);
            byte[] bytes=new byte[1024];
            List<ByteBuffer> list=new ArrayList<>();
            int b;
            while((b=is.read(bytes))!=-1){
                ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                byteBuffer.put(bytes,0,b);
                byteBuffer.flip();
                list.add(byteBuffer);
            }
            ByteBuffer[] byteBuffers=list.toArray(new ByteBuffer[list.size()]);
            wrapper.write(new ResponseContentImpl(new ByteBuffsMessage(byteBuffers)));
            wrapper.flush();
        } catch (IOException e) {
            try {
                wrapper.socketChannel.close();
            } catch (IOException ignore) {}
        }
    }

    private InputStream getHttpBack(HttpRequest request, SocketChannelWrapper wrapper) throws IOException {
        URL url=new URL(request.getRequestPath());
        return url.openConnection().getInputStream();
    }
}
