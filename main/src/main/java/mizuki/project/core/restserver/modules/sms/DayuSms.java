package mizuki.project.core.restserver.modules.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * Created by ycj on 16/8/7.
 * 阿里大于  sms
 */
@Component
@ConfigurationProperties("mod.sms_dayu")
public class DayuSms {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String product = "Dysmsapi";
    private static final String domain = "dysmsapi.aliyuncs.com";
    private String accessKey;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

    private IAcsClient acsClient;

    @PostConstruct
    private void init() throws ClientException {
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        acsClient = new DefaultAcsClient(profile);
    }

    public String send(String phone) {
        String code = genCode();

        try {
            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + code + "\"}");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return code;
            }else{
                logger.error("短信发送 error: "+sendSmsResponse.getCode()+" ; "+sendSmsResponse.getMessage());
                return null;
            }
        }catch (Exception e){
            logger.error("err：",e);
            return null;
        }
    }

    private String genCode(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<4;i++){
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public DayuSms setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public DayuSms setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public String getSignName() {
        return signName;
    }

    public DayuSms setSignName(String signName) {
        this.signName = signName;
        return this;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public DayuSms setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        return this;
    }
}
