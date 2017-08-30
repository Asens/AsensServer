package cn.asens.handler;

import cn.asens.componet.ResponseContentImpl;
import cn.asens.componet.SocketChannelWrapper;
import cn.asens.componet.StringMessage;
import cn.asens.handler.http.HttpRequest;

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
            wrapper.write(new ResponseContentImpl(new StringMessage(s)));
            wrapper.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHttpBack(String host, int port, String message, SocketChannelWrapper wrapper) throws IOException {
        System.out.println(message);
        message=message+"\r\n";
        Socket socket=new Socket(host,port);
        OutputStream os=socket.getOutputStream();

        StringBuilder messageStr=new StringBuilder();
        messageStr.append("GET http://sltjnj.digiwater.cn/ HTTP/1.1\r\n");
        messageStr.append("Accept:text/html,*/*;q=0.8\r\n");
        messageStr.append("Accept-Encoding:gzip, deflate\r\n");
        //message.append("Connection:keep-alive\r\n");
        messageStr.append("\r\n");

        os.write(messageStr.toString().getBytes());
        os.write(message.getBytes());
        os.flush();

        InputStream is=socket.getInputStream();
        byte[] bytes=new byte[512*1024*3];
        StringBuilder str=new StringBuilder();
        int b;
        while((b=is.read(bytes))!=-1){
            String s=new String(bytes,0,b,"utf-8").replace("1ffe","").replace("\n","\r\n");
            str.append(s);
        }

        System.out.println(str.toString());
        return str.toString();
    }
}
