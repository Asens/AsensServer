package cn.asens.componet;

public class StringMessage implements Message{
    private String message;

    public StringMessage(String message) {
        this.message=message;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
