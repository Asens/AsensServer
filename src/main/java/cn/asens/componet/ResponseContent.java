package cn.asens.componet;

public interface ResponseContent {
    public static enum MessageType{
        String,
        File,
        ByteBuff,
        ByteBuffers
    }

    boolean hasStringMessage();

    boolean hasFileMessage();

    MessageType messageType();

    Message getMessage();
}
