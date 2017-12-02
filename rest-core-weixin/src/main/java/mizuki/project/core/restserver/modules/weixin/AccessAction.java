package mizuki.project.core.restserver.modules.weixin;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * Created by ycj on 16/1/5.
 * *  接入微信后 消息和事件
 */

@RestController
public class AccessAction {

    @Autowired
    private WxMpService mpService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WxMpMessageRouter router;

    @RequestMapping(value = "/wx/access",method = RequestMethod.GET)
    public String access_get(
            @RequestParam(name = "signature",
                    required = false) String signature,
            @RequestParam(name = "timestamp",
                    required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr){
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (mpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * domain/project/wx/access
     * ***/
    @RequestMapping(value = "/wx/access", method = RequestMethod.POST, produces = "text/xml;charset=UTF-8")
    public String access_post(
          @RequestBody String requestBody,
          @RequestParam("signature") String signature,
          @RequestParam("timestamp") String timestamp,
          @RequestParam("nonce") String nonce,
          @RequestParam(name = "encrypt_type",
                  required = false) String encType,
          @RequestParam(name = "msg_signature",
                  required = false) String msgSignature) throws IOException {
        if (!mpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                    requestBody, mpService.getWxMpConfigStorage(), timestamp,
                    nonce, msgSignature);
            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage
                    .toEncryptedXml(mpService.getWxMpConfigStorage());
        }
        this.logger.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }
}
