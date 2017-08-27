package cn.asens.handler.http;

import cn.asens.componet.FileMessage;
import cn.asens.componet.ResponseContentImpl;
import cn.asens.componet.SocketChannelWrapper;
import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public class HttpResponse implements Response{
    private Log log= LoggerFactory.getInstance();
    private HttpRequest request;
    private SocketChannelWrapper channelWrapper;

    public HttpResponse(SocketChannelWrapper channelWrapper, HttpRequest request) {
        this.channelWrapper = channelWrapper;
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
        channelWrapper.socketChannel.write(buffer);
    }

    @Override
    public void send(byte[] bytes) throws IOException {
        send(bytes,0,bytes.length);
    }


    @Override
    public void close() throws IOException {
        channelWrapper.socketChannel.close();
    }

    @Override
    public SocketChannel getChannel() {
        return channelWrapper.socketChannel;
    }

    @Override
    public void sendOk() throws IOException {
        StringBuilder str=new StringBuilder();
        str.append("HTTP/1.1 200 ok\r\n");
        str.append("Server:AsensServer\r\n");
        if(request.getAccept().contains("text/css")){
            str.append("Content-type:text/css\r\n");
        }

//        if(request.getProtocol().equals("HTTP/1.1")){
//            str.append("Connection:Keep-Alive\r\n");
//        }
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
        channelWrapper.socketChannel.write(buffer);
    }

    @Override
    public void send(ByteBuffer[] arr) throws IOException {

    }

    @Override
    public void write(RandomAccessFile file){
        FileMessage fileMessage=new FileMessage(file);
        channelWrapper.write(new ResponseContentImpl(fileMessage));
    }

    @Override
    public void flush() throws IOException {
        channelWrapper.flush();
    }


}
