package cn.asens.handler;

import cn.asens.componet.ResponseContentImpl;
import cn.asens.componet.SocketChannelWrapper;
import cn.asens.componet.StringMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Asens
 * create 2017-08-23 20:30
 **/

public class DefaultHandler implements RequestHandler{


    private void writeBack(SocketChannelWrapper wrapper) throws IOException{
        String message="HTTP/1.1 200 ok\n"+
                "Content-Type:text/html\n"+
                "\n"+
                "<div style='text-align:center;padding:50px;'><h1>Welcome to AsensServer</h1>" +
                "<p>sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss</p>" +
                "</div>";
        wrapper.write(new ResponseContentImpl(new StringMessage(message)));
        wrapper.flush();
        wrapper.socketChannel.close();
    }

    @Override
    public void handle(ByteBuffer buffer, SocketChannelWrapper wrapper) {
        String message = null;
        try {
            message = new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(message);
        try {
            writeBack(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
