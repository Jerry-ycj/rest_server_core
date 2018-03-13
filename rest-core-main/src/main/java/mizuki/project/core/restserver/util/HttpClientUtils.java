package mizuki.project.core.restserver.util;

import mizuki.project.core.restserver.config.BasicRet;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private static CloseableHttpClient httpclient= HttpClients.createDefault();
    public static Map<String,Object> post(String url, Map<String,String> params){
        HttpPost post=new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        params.keySet().forEach((key) -> pairs.add(new BasicNameValuePair(key,params.get(key))));
        Map<String,Object> res=null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
            post.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            res = JsonUtil.toMap(EntityUtils.toString(entity));
            EntityUtils.consume(entity);
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res==null) res = new BasicRet(BasicRet.ERR,"post调用报错").map();
        return res;
    }
}
