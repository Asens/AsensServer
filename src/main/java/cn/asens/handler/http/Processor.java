package cn.asens.handler.http;

/**
 * Created by Asens on 2017/8/25.
 */
public interface Processor {
    void process(Request request,Response response);
}
