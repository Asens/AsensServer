package cn.asens.componet;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

import java.io.IOException;
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

        for (ResponseContent responseContent : queue) {
            if (responseContent.hasFileMessage()) {
                FileMessage message = (FileMessage) responseContent.getMessage();
                flush(message);
            }
        }

        if(queue.isEmpty()){
            clearOpWrite();
        }

        if (complete()) {
            log.debug("all complete socket close");
            //TODO if use http/1.1,should not close here
            socketChannel.close();
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
