package cn.asens.componet;

public interface Message {
    boolean isDone();

    long getPosition();

    void setPosition(long position);

    long getTotalCount();

    void setDone(boolean b);
}
