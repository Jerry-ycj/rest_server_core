package mizuki.project.core.restserver.modules.sms;

import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.UserMapper;
import mizuki.project.core.restserver.modules.oss_ali.AliOSS;
import mizuki.project.core.restserver.modules.sms.DayuSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

/**
 * 共用的一些 Controller
 */
@RestController
@RequestMapping("/rest/common/sms")
public class CommonSmsRestAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DayuSms dayuSms;

    @RequestMapping(value="")
    public Map<String, Object> smsCode(
            @RequestParam String phone
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            String code = dayuSms.send(phone);
            if(code==null){
                data.put("result", 0);
                data.put("message","短信发送失败");
                return data;
            }
            if(userMapper.findSmsCode(phone)!=null){
                userMapper.updateSmsCode(phone,code);
            }else{
                userMapper.saveSmsCode(phone,code);
            }
            return data;
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }

}
