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
//        configStorage.setAesKey(this.properties.getAesKey());
        configStorage.setPartnerId(partnerid);
        configStorage.setPartnerKey(partnerkey);
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

    /*
    @Bean
    public WxMpMessageRouter router(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // async(false) 同步执行,表示执行完再返回给微信

        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_CREATE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_CLOSE_SESSION).handler(this.kfSessionHandler)
//                .end();
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_SWITCH_SESSION)
//                .handler(this.kfSessionHandler).end();

        // 自定义菜单事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.BUTTON_CLICK).handler(this.getMenuHandler()).end();

        // 点击菜单连接事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.BUTTON_VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE).handler(this.subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_UNSUBSCRIBE)
                .handler(this.unsubscribeHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
     */

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
