package cn.asens.http;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public class HttpResponse implements Response{
    private SocketChannel channel;

    public HttpResponse(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendError(int status) {
        String error="";
    }

    @Override
    public void send(String message) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        buffer.put(message.getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    @Override
    public void send(byte[] bytes) throws IOException {
        send(new String(bytes,"UTF-8"));
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public SocketChannel getChannel() {
        return channel;
    }
}
