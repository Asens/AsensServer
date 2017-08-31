package cn.asens.handler;

import cn.asens.componet.ResponseContentImpl;
import cn.asens.componet.SocketChannelWrapper;
import cn.asens.componet.StringMessage;
import cn.asens.handler.http.HttpRequest;
import cn.asens.util.HttpUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by Asens on 2017/8/30.
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
        if(message!=null&&message.contains("google")) return;
        HttpRequest request = new HttpRequest(message);
        request.parseHttpRequest();
        String host = request.getHost();
        int port = 80;

        try {
            String s=getHttpBack(host,port,message,wrapper);
            s=s.replace("Transfer-Encoding:chunked\n","");
            System.out.println(s);
            wrapper.write(new ResponseContentImpl(new StringMessage(s)));
            wrapper.flush();
        } catch (IOException e) {
            try {
                wrapper.socketChannel.close();
            } catch (IOException ignore) {}
        }
    }

    private String getHttpBack(String host, int port, String message, SocketChannelWrapper wrapper) throws IOException {
        return HttpUtils.doGet("http://127.0.0.1/",null);
    }
}
