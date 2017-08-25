package cn.asens.handler.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public class HttpResponse implements Response{
    private Log log= LoggerFactory.getInstance();
    private HttpRequest request;
    private SocketChannel channel;

    public HttpResponse(SocketChannel channel, HttpRequest request) {
        this.channel = channel;
        this.request=request;
    }

    @Override
    public void sendError(int status) {
        String error="HTTP/1.1 "+status+" error\r\n"+
                "Content-Type:text/html\r\n"+
                "\r\n"+
                "<h1>404</h1>";
        try {
            send(error);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        try {
            buffer.put(message.getBytes());
        }catch (BufferOverflowException e){
            log.error(e.getMessage());
        }
        buffer.flip();
        channel.write(buffer);
    }

    @Override
    public void send(byte[] bytes) throws IOException {
        send(bytes,0,bytes.length);
    }


    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public void sendOk() throws IOException {
        StringBuilder str=new StringBuilder();
        str.append("HTTP/1.1 200 ok\r\n");
        str.append("Server:AsensServer\r\n");
        if(request.getAccept().contains("text/css")){
            str.append("Content-type:text/css\r\n");
        }
        str.append("\r\n");
        send(str.toString());
    }

    @Override
    public void send(byte[] bytes, int index, int length) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        try {
            buffer.put(bytes,index,length);
        }catch (BufferOverflowException e){
            log.error(e.getMessage());
        }
        buffer.flip();
        channel.write(buffer);
    }

    @Override
    public void send(ByteBuffer[] arr) throws IOException {
        long writeBytes=channel.write(arr);
        log.debug("writeBytes "+writeBytes);
    }
}
