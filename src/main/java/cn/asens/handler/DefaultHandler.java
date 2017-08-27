package cn.asens.handler;

import cn.asens.componet.SocketChannelWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Asens
 * create 2017-08-23 20:30
 **/

public class DefaultHandler implements RequestHandler{


    private void writeBack(SocketChannel channel) throws IOException{
        String errorMessage="HTTP/1.1 200 ok\r\n"+
                "Content-Type:text/html\r\n"+
                "\r\n"+
                "<div style='text-align:center;padding:50px;'><h1>Welcome to AsensServer</h1></div>";
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        buffer.put(errorMessage.getBytes());
        buffer.flip();
        channel.write(buffer);
        channel.close();
    }

    @Override
    public void handle(ByteBuffer buffer, SocketChannelWrapper wrapper) {
//        String message = null;
//        try {
//            message = new String(buffer.array(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(message);
        try {
            writeBack(wrapper.socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
