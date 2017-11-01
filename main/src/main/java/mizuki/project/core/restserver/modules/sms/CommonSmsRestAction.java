package mizuki.project.core.restserver.modules.sms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 共用的一些 Controller
 */
@RestController
@RequestMapping("/rest/common")
@Api(tags = "通用模块")
public class CommonSmsRestAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SmsMapper smsMapper;
    @Autowired
    private DayuSms dayuSms;

    @RequestMapping(value="/sms",method = RequestMethod.POST)
    @ApiOperation(value = "短信发送")
    public BasicRet smsCode(
            @RequestParam String phone
    ) throws RestMainException {
        try{
            String code = dayuSms.send(phone);
            if(code==null){
                return new BasicRet(BasicRet.ERR,"短信发送失败");
            }
            if(smsMapper.findSmsCode(phone)!=null){
                smsMapper.updateSmsCode(phone,code);
            }else{
                smsMapper.saveSmsCode(phone,code);
            }
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }

}
