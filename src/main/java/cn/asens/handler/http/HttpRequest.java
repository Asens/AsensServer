package cn.asens.handler.http;

import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;
import cn.asens.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
    private String accept;
    private Map<String,String> paramMap;
    private static Log log= LoggerFactory.getInstance();
    public HttpRequest(String message){
        this.message=message;
    }

    public void parseHttpRequest() {
        if(StringUtils.isBlank(message)){
            log.error("http request is empty or null");
            throw new HttpParseException("http request is empty or null");
        }
        paramMap=parseParamMap(message);

        String[] arr=message.split("\r\n");
        String info=arr[0];
        String[] tem=info.split(" ");
        method=tem[0];
        requestPath=tem[1];
        protocol=tem[2];
        accept=paramMap.get("Accept");
        host=paramMap.get("Host").replace(" ","");
    }

    private Map<String, String> parseParamMap(String message) {
        Map<String,String> paramMap=new HashMap<>();
        String[] arr=message.split("\r\n");
        for(String s:arr){
            if(StringUtils.isBlank(s)) continue;
            if(!s.contains(":")) continue;
            String[] tem=s.split(":");
            paramMap.put(tem[0],tem[1]);
        }
        return paramMap;
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

    @Override
    public String getAccept() {
        return accept;
    }

    @Override
    public String getParam(String param) {
        return paramMap.get(param);
    }

}
