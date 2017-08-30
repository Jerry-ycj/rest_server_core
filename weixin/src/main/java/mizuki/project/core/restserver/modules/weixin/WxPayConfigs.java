package mizuki.project.core.restserver.modules.weixin;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(WxPayService.class)
@ConfigurationProperties("mod.weixin.pay")
public class WxPayConfigs {

    @Autowired
    private WxMpConfigStorage configStorage;
    private String mchId;
    private String mchKey;
    private String subAppId;
    private String subMchId;
    private String keyPath;

    @Bean
    @ConditionalOnMissingBean
    public WxPayConfig config() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(configStorage.getAppId());
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
//        payConfig.setSubAppId(this.properties.getSubAppId());
//        payConfig.setSubMchId(this.properties.getSubMchId());
//        payConfig.setKeyPath(this.properties.getKeyPath());

        return payConfig;
    }

    @Bean
    public WxPayService wxPayService(WxPayConfig payConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    public WxPayConfigs setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public WxPayConfigs setMchKey(String mchKey) {
        this.mchKey = mchKey;
        return this;
    }

    public WxPayConfigs setSubAppId(String subAppId) {
        this.subAppId = subAppId;
        return this;
    }

    public WxPayConfigs setSubMchId(String subMchId) {
        this.subMchId = subMchId;
        return this;
    }

    public WxPayConfigs setKeyPath(String keyPath) {
        this.keyPath = keyPath;
        return this;
    }
}
