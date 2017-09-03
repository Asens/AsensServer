package cn.asens.componet;

/**
 * @author Asens
 * create 2017-08-27 13:07
 **/

public class ResponseContentImpl implements ResponseContent{
    private Message message;
    private MessageType messageType;

    public ResponseContentImpl(String s){
        message=new StringMessage(s);
        this.messageType=MessageType.String;
    }

    public ResponseContentImpl(Message message){
        this.message=message;
        if(message instanceof StringMessage)
            this.messageType=MessageType.String;
        else if(message instanceof FileMessage)
            this.messageType=MessageType.File;
        else if(message instanceof ByteBuffMessage)
            this.messageType=MessageType.ByteBuff;
        else if(message instanceof ByteBuffsMessage)
            this.messageType=MessageType.ByteBuffers;
    }


    @Override
    public boolean hasStringMessage() {
        return (message instanceof StringMessage);
    }

    @Override
    public boolean hasFileMessage() {
        return (message instanceof FileMessage);
    }

    @Override
    public MessageType messageType() {
        return messageType;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
