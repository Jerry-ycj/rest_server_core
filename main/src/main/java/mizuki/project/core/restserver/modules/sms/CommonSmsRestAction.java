package mizuki.project.core.restserver.modules.sms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.UserMapper;
import mizuki.project.core.restserver.modules.oss_ali.AliOSS;
import mizuki.project.core.restserver.modules.sms.DayuSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

/**
 * 共用的一些 Controller
 */
@RestController
@RequestMapping("/rest/common")
@Api(tags = "通用模块")
public class CommonSmsRestAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;
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
            if(userMapper.findSmsCode(phone)!=null){
                userMapper.updateSmsCode(phone,code);
            }else{
                userMapper.saveSmsCode(phone,code);
            }
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }

}
