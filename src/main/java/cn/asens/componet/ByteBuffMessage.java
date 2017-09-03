package cn.asens.componet;

import java.nio.ByteBuffer;

/**
 * Created by Asens on 2017/9/3
 */
public class ByteBuffMessage implements Message{
    private long position=0;
    private long totalCount;
    private boolean done;
    private ByteBuffer byteBuffer;

    public ByteBuffMessage(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        totalCount=byteBuffer.array().length;
    }

    public ByteBuffer message(){
        return byteBuffer;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
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
        return 0;
    }
}
