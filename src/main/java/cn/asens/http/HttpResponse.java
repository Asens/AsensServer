package cn.asens.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public class HttpResponse implements Response{
    private Log log= LoggerFactory.getInstance();

    private SocketChannel channel;

    public HttpResponse(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendError(int status) {
        String error="HTTP/1.1 "+status+" ok\r\n"+
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
        String message="HTTP/1.1 200 ok\r\n"+
                //"Content-Type:text/html\r\n"+
                "\r\n";
        send(message);
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
}
