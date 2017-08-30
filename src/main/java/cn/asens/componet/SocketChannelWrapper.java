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
            flush(responseContent);
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

    private void flush(ResponseContent responseContent) throws IOException{
        switch (responseContent.messageType()){
            case String:
                flush((StringMessage) responseContent.getMessage());
                break;
            case File:
                flush((FileMessage) responseContent.getMessage());
                break;
            case ByteBuff:
                //TODO
                log.warn("not support now");
                break;
            case ByteBuffers:
                //TODO
                log.warn("not support now");
                break;
            default:
                log.error("should not be here");
                throw new Error();
        }
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
        int size=bytes.length/1024+1;
        ByteBuffer[] buffers=new ByteBuffer[size];
        for(int i=0;i<size;i++){
            buffers[i]=ByteBuffer.allocate(1024);
            byte[] tem=new byte[1024];
            System.arraycopy(bytes,i*1024,tem,0,i==size-1?(bytes.length-1024*(size-1)):1024);
            buffers[i].put(tem);
            buffers[i].flip();
        }
        log.warn("message out of 1024,handle it later");
        return buffers;
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
