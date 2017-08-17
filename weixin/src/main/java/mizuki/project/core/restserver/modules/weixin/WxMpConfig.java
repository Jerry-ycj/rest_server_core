package mizuki.project.core.restserver.modules.weixin;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ycj on 16/5/9.
 * 外部采用 @Import 方式导入
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
@ConfigurationProperties("mod.weixin.mp")
public class WxMpConfig {

    private String appid;
    private String secret;
    private String token;
    private String partnerid;
    private String partnerkey;

    @Bean
    public WxMpConfigStorage configStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(appid);
        configStorage.setSecret(secret);
        configStorage.setToken(token);
        return configStorage;
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

    @Bean
    public WxTemplateUtil wxTemplateUtil(){
        return new WxTemplateUtil();
    }

    @Bean
    public AccessAction accessAction(){
        return new AccessAction();
    }

    public WxMpConfig setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public WxMpConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public WxMpConfig setToken(String token) {
        this.token = token;
        return this;
    }

    public WxMpConfig setPartnerid(String partnerid) {
        this.partnerid = partnerid;
        return this;
    }

    public WxMpConfig setPartnerkey(String partnerkey) {
        this.partnerkey = partnerkey;
        return this;
    }
}
