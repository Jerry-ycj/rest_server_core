package mizuki.project.core.restserver.modules.weixin;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ycj on 2016/11/14.
 * 模板消息
 */
public class WxTemplateUtil {

    private WxMpService wxMpService;
    @Autowired
    private WxMpConfig config;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String COLOR_BLUE = "#0000FF";
    public static final String COLOR_ORANGE = "#FF7F00";
    public static final String COLOR_FGREEN = "#238E23";

    @Autowired
    public WxTemplateUtil setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
        return this;
    }

    /**
     * 模板信息
     */
    public void notice(String content1,String[] ids,String url,String color){
        List<String> list = new ArrayList<>();
        Collections.addAll(list, ids);
        notice(content1,list,url,color);
    }
    /**
     * 模板信息 - 订单消息通知 模板
     */
    public void notice(String content1,List<String> ids,String url,String color){
        String now = LocalDate.now().toString();
        for(String openid:ids){
            if(openid==null){
                System.out.println("openid null");
                continue;
            }
            WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
            templateMessage.setToUser(openid);
            templateMessage.setTemplateId(config.getTemplateIdNotice());
            templateMessage.setUrl(url);
//        templateMessage.setTopColor();
            templateMessage.getData().add(new WxMpTemplateData("first", content1, color));
            templateMessage.getData().add(new WxMpTemplateData("keyword2", now, "#000000"));
            try {
                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            } catch (WxErrorException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
