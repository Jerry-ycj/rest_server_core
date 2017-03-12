package mizuki.project.core.restserver.modules.sms;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * Created by ycj on 16/8/7.
 * 阿里大于  sms
 */
@ConfigurationProperties("mod.sms_dayu")
public class DayuSms {

    private String appkey;
    private String appSecret;
    private String template_reg;
    private String product_name;

    public String send(String phone) throws ApiException {
        String code = genCode();
        String url = "http://gw.api.taobao.com/router/rest";
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("注册验证");
        req.setSmsParam("{\"code\":\""+code+"\",\"product\":\""+product_name+"\"}");
        req.setRecNum(phone);
        req.setSmsTemplateCode(template_reg);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        String ret = rsp.getBody();
        System.out.println(ret);
        if(ret.contains("\"success\":true")){
            return code;
        }
//        else{
//            int i=5;
//            while (i>0){
//                rsp = client.execute(req);
//                ret = rsp.getBody();
//                System.out.println(ret);
//                i--;
//                if(ret.contains("\"success\":true")){
//                    return code;
//                }
//            }
//        }
//			System.out.println(rsp.getBody());  TODO json 判断success
        return null;
    }

    private String genCode(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<4;i++){
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    public DayuSms setAppkey(String appkey) {
        this.appkey = appkey;
        return this;
    }

    public DayuSms setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public DayuSms setTemplate_reg(String template_reg) {
        this.template_reg = template_reg;
        return this;
    }

    public DayuSms setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }
}
