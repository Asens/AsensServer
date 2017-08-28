package cn.asens.componet;

public class StringMessage implements Message{
    private String message;
    private long totalCount;
    private long position=0;
    private boolean done;

    public StringMessage(String message) {
        this.message=message;
        totalCount=message.getBytes().length;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
