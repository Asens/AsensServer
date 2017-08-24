package cn.asens.http;

/**
 * Created by Asens on 2017/8/24.
 */
public class HttpParseException extends RuntimeException {
    public HttpParseException() {
    }

    public HttpParseException(String message) {
        super(message);
    }
}
