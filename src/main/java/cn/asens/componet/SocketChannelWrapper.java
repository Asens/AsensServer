package cn.asens.componet;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Asens
 * create 2017-08-26 9:48
 **/

public class SocketChannelWrapper {
    public final SocketChannel socketChannel;
    private Queue<ResponseContent> queue=new ConcurrentLinkedQueue<>();
    private SelectionKey selectionKey;
    private static Log log= LoggerFactory.getInstance();

    public SocketChannelWrapper(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void add(ResponseContent responseContent){
        queue.add(responseContent);
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public ResponseContent peek(){
        return queue.peek();
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void write(ResponseContent responseContent){
        queue.add(responseContent);
    }

    public void flush() throws IOException {
        log.debug("queue length "+queue.size());
        for (ResponseContent responseContent : queue) {
            if (responseContent.hasFileMessage()) {
                FileMessage message = (FileMessage) responseContent.getMessage();
                flush(message);
            }else if(responseContent.hasStringMessage()){
                StringMessage message=(StringMessage) responseContent.getMessage();
                flush(message);
            }
        }

        if (complete()) {
            log.debug(Thread.currentThread().getName()+ " all complete socket close --"+socketChannel);
            //socketChannel.close();
        }

        if(queue.isEmpty()){
            clearOpWrite();
        }
    }




    private boolean complete() {
        for(ResponseContent responseContent:queue){
            if(!responseContent.getMessage().isDone()) {
                return false;
            }else{
                log.debug("remove the done responseContent in queue");
                queue.remove(responseContent);
            }
        }
        return true;
    }

    private void flush(FileMessage message) throws IOException {
        FileChannel fileChannel=message.message();
        for(int i=0;i<16;i++){
            long written=fileChannel.transferTo(message.getPosition(),message.getTotalCount(),socketChannel);
            log.debug("written : "+written);
            message.setPosition(message.getPosition()+written);
            if(message.getPosition()==message.getTotalCount()){
                message.setDone(true);
                break;
            }
            if(written==0){
                registerWrite();
                break;
            }
        }
    }

    private void flush(StringMessage message) throws IOException {
        String str=message.message();
        ByteBuffer[] byteBuffers=toBuffers(str);
        for(int i=0;i<16;i++){
            long written=socketChannel.write(byteBuffers);
            message.setPosition(message.getPosition()+written);
            if(message.getPosition()==message.getTotalCount()){
                message.setDone(true);
                break;
            }
            if(written==0){
                registerWrite();
                break;
            }
        }
    }

    private ByteBuffer[] toBuffers(String str) {
        byte[] bytes=str.getBytes();
        if(bytes.length<1024){
            ByteBuffer[] byteBuffers=new ByteBuffer[1];
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            buffer.put(bytes);
            buffer.flip();
            byteBuffers[0]=buffer;
            return byteBuffers;
        }
        //TODO
        log.warn("message out of 1024,handle it later");
        return new ByteBuffer[0];
    }

    private void registerWrite() {
        SelectionKey key=getSelectionKey();
        final int interestOps = key.interestOps();
        if ((interestOps & SelectionKey.OP_WRITE) == 0) {
            key.interestOps(interestOps | SelectionKey.OP_WRITE);
        }
    }

    private void clearOpWrite() {
        SelectionKey key=getSelectionKey();
        int interestOps = key.interestOps();
        if ((interestOps & SelectionKey.OP_WRITE) != 0) {
            key.interestOps(interestOps & ~SelectionKey.OP_WRITE);
        }
    }
}
