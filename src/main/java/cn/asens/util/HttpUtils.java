package cn.asens.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author Asens
 * create 2017-08-30 22:16
 **/

public class HttpUtils {
    public static String doGet(String uri, List<NameValuePair> parameters){
        HttpGet httpGet = new HttpGet(uri);
        String param = null;
        try{
            //param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
            //build get uri with params
            httpGet.setURI(new URIBuilder(httpGet.getURI().toString() ).build());
        }catch(Exception e){
            e.printStackTrace();
        }
        return sendHttpGet(httpGet);
    }


    private static String sendHttpGet(HttpGet httpGet){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        StringBuilder str=new StringBuilder();
        String responseContent = null;
        try{
            httpClient = HttpClients.createDefault();
//          httpGet.setConfig(config);
            response = httpClient.execute(httpGet);

            str.append(response.getStatusLine()).append("\n");

            for(Header header:response.getAllHeaders()){
                str.append(header.getName()).append(":").append(header.getValue()).append("\n");
            }
            str.append("\n");

            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            str.append(responseContent);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(response != null)
                    response.close();
                if(httpClient != null)
                    httpClient.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return str.toString();
    }
}
