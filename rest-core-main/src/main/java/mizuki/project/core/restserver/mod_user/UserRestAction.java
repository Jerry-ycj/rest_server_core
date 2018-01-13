package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.WebConfBean;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.mod_user.bean.ret.RoleListRet;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.modules.sms.SmsMapper;
import mizuki.project.core.restserver.util.CodeUtil;
import mizuki.project.core.restserver.util.IOUtil;
import mizuki.project.core.restserver.util.OtherUtil;
import mizuki.project.core.restserver.util.WebIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/user")
@SessionAttributes({"user"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理用户模块",description = "管理的用户")
public class UserRestAction{

	@Autowired
	private UserMapper userMapper;
	@Autowired
    private SmsMapper smsMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 头像 */
    private static final String imgPath="/user/image";

    @RequestMapping(value = "/listRoles",method = RequestMethod.POST)
    @ApiOperation(value = "获取角色列表")
    public RoleListRet listRoles() throws RestMainException{
        RoleListRet ret = new RoleListRet();
        try{
            ret.getData().setRoles(userMapper.listRoles());
            ret.setResult(BasicRet.SUCCESS);
            return ret;
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ApiOperation(value = "登出")
    public BasicRet logout(
            Model model,
            HttpSession session
    ) throws RestMainException {
        try{
            model.asMap().remove("user");
            session.removeAttribute("user");
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

//	@RequestMapping(value="/register")
	public Map<String, Object> userRegister(
            Model model,
            @RequestParam String pwd,
            @RequestParam String phone,
            @RequestParam String sms,
            @RequestParam int role
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            Role r = userMapper.findRole(role);
            if(r==null){
                data.put("result", 0);
                data.put("message", "role err");
                return data;
            }
            if(userMapper.findUserByPhone(phone)!=null){
                data.put("result", 0);
                data.put("message", "手机已注册");
                return data;
            }
            if(!sms.equals(smsMapper.findSmsCode(phone))){
                data.put("result", 0);
                data.put("message", "验证码错误");
                return data;
            }
            User user = new User()
                    .setPwd(CodeUtil.md5(pwd))
                    .setPhone(phone).setRole(r);
            userMapper.saveUser(user);
            String token = OtherUtil.get32UUID();
            /** 注意, 只在这里save了,其他地方是update */
            userMapper.saveRestToken(user.getId(),token);

            model.addAttribute("user",user);
            data.put("result", 1);
            data.put("token", token);
            data.put("user", user);
            data.put("message", "成功");
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
	}
	

	@RequestMapping(value="/loginByPhone",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录（手机）")
	public LoginUserRet userLogin(
            @RequestParam String phone,
            @RequestParam String pwd,
            Model model
    ) throws RestMainException {
        LoginUserRet ret=new LoginUserRet();
        try{
            String passwd = CodeUtil.md5(pwd);
            User user = userMapper.loginByPhone(phone,passwd);
            if(user != null){
                return loginHandle(user,ret,model);
            }else{
                return (LoginUserRet)ret.setResult(BasicRet.ERR).setMessage("用户名或密码错误");
            }
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
	}

    @RequestMapping(value = "/loginByUsername",method = RequestMethod.POST)
    @ApiOperation(value = "用户名登录")
    public LoginUserRet login(
            Model model,
            @RequestParam String username,
            @RequestParam String pwd
    ) throws RestMainException{
        LoginUserRet ret=new LoginUserRet();
        try{
            String passwd = CodeUtil.md5(pwd);
            User user = userMapper.loginByUsername(username,passwd);
            if(user != null){
                return loginHandle(user,ret,model);
            }else{
                return (LoginUserRet)ret.setResult(BasicRet.ERR).setMessage("用户名或密码错误");
            }
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping(value="/loginSms",method = RequestMethod.POST)
    @ApiOperation(value = "短信登录")
    public LoginUserRet userLoginSms(
            @RequestParam String phone,
            @RequestParam String sms,
            Model model
    ) throws RestMainException {
        LoginUserRet ret=new LoginUserRet();
        try{
            if(!sms.equals(smsMapper.findSmsCode(phone))){
                return (LoginUserRet) ret.setResult(BasicRet.ERR).setMessage("验证码错误");
            }
            User user = userMapper.findUserByPhone(phone);
            if(user==null){
                return (LoginUserRet) ret.setResult(BasicRet.ERR).setMessage("用户不存在");
            }
            return loginHandle(user,ret,model);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    /***
     * 登录时  获取user 和 systems
     */
    private LoginUserRet loginHandle(User user,LoginUserRet ret,Model model){
        if(user.getOff()==User.OFF_FREEZE){
            return (LoginUserRet) ret.setResult(BasicRet.ERR).setMessage("账户被冻结");
        }
//        else if(user.getOff()==2){
//            return (LoginUserRet) ret.setResult(BasicRet.ERR).setMessage("账户审核中");
//        }
        String token = OtherUtil.get32UUID();
        if(userMapper.findRestToken(user.getId())==null){
            userMapper.saveRestToken(user.getId(),token);
        }else {
            userMapper.updateRestToken(user.getId(), token);
        }
        model.addAttribute("user",user);
        ret.token = token;
        ret.user = user;
        return (LoginUserRet) ret.setResult(BasicRet.SUCCESS);
    }
    private class LoginUserRet extends BasicRet{
        private String token;
        private User user;

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }


    @RequestMapping(value="/updatePwd",method = RequestMethod.POST)
    @ApiOperation(value = "密码修改")
    public BasicRet updateUserPassword(
            Model model,
            @RequestParam String oldPwd,
            @RequestParam String newPwd
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        try{
            if(!user.getPwd().equals(CodeUtil.md5(oldPwd))){
                return new BasicRet(BasicRet.ERR,"密码错误");
            }
            user.setPwd(CodeUtil.md5(newPwd));
            userMapper.updateUser(user);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping(value="/updateUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息")
    public BasicRet updateUserInfo(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam(required = false)String address,
            @RequestParam(required = false)String sms
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        try{
            if(phone!=null && !phone.equals(user.getPhone()) && sms==null){
                return new BasicRet(BasicRet.ERR,"修改手机需要验证码");
            }
            if(sms!=null && phone!=null && !phone.equals(user.getPhone())
                    && !sms.equals(smsMapper.findSmsCode(phone))){
                return new BasicRet(BasicRet.ERR,"验证码错误");
            }
            if(name!=null) user.setName(name);
            if(gender!=0) user.setGender(gender);
            if(address!=null) user.setAddress(address);
            userMapper.updateUser(user);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }


//    @RequestMapping(value="/updateUserImage",method = RequestMethod.POST)
    public Map<String, Object> updateUserImage(
            Model model,
            @RequestParam MultipartFile image
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        WebConfBean wcb = (WebConfBean)model.asMap().get("confBean");
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            String filename = user.getId()+"_"+ LocalDateTime.now().toString()+IOUtil.getSuffixFromMIME(image.getContentType());
            String uimage = imgPath+"/"+filename;
            File file = IOUtil.prepare(wcb.getProjectPath()+imgPath,filename);
            IOUtil.saveStream2File(image.getInputStream(),file);
            user.setImage(uimage);
            userMapper.updateUser(user);
            // 保存成功
            user.setImage(uimage);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

//    @RequestMapping(value = "/download/{code}",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(
            Model model,
            @PathVariable String code)
            throws IOException {
        WebConfBean wcb = (WebConfBean)model.asMap().get("confBean");
        return WebIOUtil.downloadFileWithCode(
                wcb.getProjectPath(), "image",code,
                imgPath);
    }

    @RequestMapping(value="/resetPassword",method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public BasicRet resetPassword(
            @RequestParam String sms,
            @RequestParam String phone,
            @RequestParam String newPwd
    ) throws RestMainException {
        try{
            if(!sms.equals(smsMapper.findSmsCode(phone))){
                return new BasicRet(BasicRet.ERR,"验证码错误");
            }
            User user = userMapper.findUserByPhone(phone);
            if(user==null){
                return new BasicRet(BasicRet.ERR,"用户不存在");
            }
            user.setPwd(CodeUtil.md5(newPwd));
            userMapper.updateUser(user);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }
}
