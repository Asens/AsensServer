package cn.asens.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;
import cn.asens.util.StringUtils;

/**
 * Created by Asens on 2017/8/24.
 */
public class HttpRequest implements Request{
    private String message;
    private String method;
    private String requestPath;
    private String protocol;
    private String host;
    private String connection;
    private static Log log= LoggerFactory.getInstance();
    public HttpRequest(String message){
        this.message=message;
    }

    private void parseHttpRequest() {
        if(StringUtils.isBlank(message)){
            log.error("http request is empty or null");
            throw new HttpParseException("http request is empty or null");
        }


    }

    public HttpRequest(byte[] bytes){

    }



    @Override
    public String getMethod() {
        return method;
    }
    @Override
    public String getRequestPath() {
        return requestPath;
    }
    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

}
