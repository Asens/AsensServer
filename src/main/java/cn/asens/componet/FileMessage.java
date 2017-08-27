package cn.asens.componet;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author Asens
 * create 2017-08-27 13:18
 **/

public class FileMessage implements Message{
    private FileChannel fileChannel;
    private long position=0;
    private long totalCount;
    private boolean done;

    public FileMessage(RandomAccessFile file) {
        this.fileChannel =file.getChannel();
        try {
            totalCount=file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileChannel message(){
        return fileChannel;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
