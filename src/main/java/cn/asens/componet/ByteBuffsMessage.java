package cn.asens.componet;

import java.nio.ByteBuffer;

/**
 * Created by Asens on 2017/9/3.
 */
public class ByteBuffsMessage implements Message{
    private long position=0;
    private long totalCount=0;
    private boolean done;
    private final ByteBuffer[] byteBuffers;

    public ByteBuffsMessage(ByteBuffer[] byteBuffers) {
        this.byteBuffers = byteBuffers;
        for(ByteBuffer buffer:byteBuffers){
            totalCount+=buffer.array().length;
        }

    }

    public ByteBuffer[] message(){
        return byteBuffers;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public void setPosition(long position) {
        this.position=position;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public void setDone(boolean done) {
        this.done=done;
    }
}
