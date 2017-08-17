package mizuki.project.core.restserver.mod_user;

import mizuki.project.core.restserver.config.WebConfBean;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
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
public class UserRestAction{

	@Autowired
	private UserMapper userMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 头像 */
    private static final String imgPath="/user/image";

    /** 获取角色列表 */
    @RequestMapping("/listRoles")
    public Map listRoles() throws RestMainException{
        Map<String,Object> data = new HashMap<>();
        try{
            List<Role> roles = userMapper.listRoles();
            data.put("result",1);
            data.put("roles",roles);
            return data;
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }

    @RequestMapping("/logout")
    public Map logout(
            Model model,
            HttpSession session
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            model.asMap().remove("user");
            session.removeAttribute("user");
            data.put("result",1);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }
	
	/**
	 * 用户注册
	 */
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
            if(!sms.equals(userMapper.findSmsCode(phone))){
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
	
	/**
     * 用户登录
	 */
	@RequestMapping(value="/loginByPhone")
	public Map<String, Object> userLogin(
            @RequestParam String phone,
            @RequestParam String pwd,
            Model model
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            String passwd = CodeUtil.md5(pwd);
            User user = userMapper.loginByPhone(phone,passwd);
            if(user != null){
                loginHandle(user,data,model);
            }else{
                data.put("result", 0);
                data.put("message", "用户名或密码错误");
            }
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
	}

    @RequestMapping("/loginByUsername")
    public Map login(
            Model model,
            @RequestParam String username,
            @RequestParam String pwd
    ) throws RestMainException{
        Map<String,Object> data = new HashMap<>();
        try{
            String passwd = CodeUtil.md5(pwd);
            User user = userMapper.loginByUsername(username,passwd);
            if(user != null){
                loginHandle(user,data,model);
            }else{
                data.put("result",0);
                data.put("message","用户名或密码错误");
            }
            return data;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("err",e);
            throw new RestMainException(e);
        }
    }

    /**
     * 用户登录 sms code
     */
    @RequestMapping(value="/loginSms")
    public Map<String, Object> userLoginSms(
            @RequestParam String phone,
            @RequestParam String sms,
            Model model
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            if(!sms.equals(userMapper.findSmsCode(phone))){
                data.put("result", 0);
                data.put("message", "验证码错误");
                return data;
            }
            User user = userMapper.findUserByPhone(phone);
            if(user==null){
                data.put("result", 0);
                data.put("message", "用户不存在");
                return data;
            }
            loginHandle(user,data,model);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    /***
     * 登录时  获取user 和 systems
     */
    private void loginHandle(User user,Map<String,Object> data,Model model){
        if(user.getOff()==1){
            data.put("result", 0);
            data.put("message", "账户被冻结");
            return ;
        }else if(user.getOff()==2){
            data.put("result", 0);
            data.put("message", "账户审核中");
            return ;
        }
        String token = OtherUtil.get32UUID();
        if(userMapper.findRestToken(user.getId())==null){
            userMapper.saveRestToken(user.getId(),token);
        }else {
            userMapper.updateRestToken(user.getId(), token);
        }
        model.addAttribute("user",user);
        data.put("result", 1);
        data.put("token", token);
        data.put("user", user);
        data.put("message", "成功");
    }

    /**
     * 密码修改
     */
    @RequestMapping(value="/updatePwd")
    public Map<String, Object> updateUserPassword(
            Model model,
            @RequestParam String oldPwd,
            @RequestParam String newPwd
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            if(!user.getPwd().equals(CodeUtil.md5(oldPwd))){
                data.put("result", 0);
                data.put("message", "用户名或密码错误");
                return data;
            }
            user.setPwd(CodeUtil.md5(newPwd));
            userMapper.updateUserPassword(user);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    /**
     * 更新.
     */
    @RequestMapping(value="/updateUserInfo")
    public Map<String, Object> updateUserInfo(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam(required = false)String address,
            @RequestParam(required = false)String sms
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        Map<String,Object> data = new HashMap<>();
        try{
            if(phone!=null && !phone.equals(user.getPhone()) && sms==null){
                data.put("result", 0);
                data.put("message", "修改手机需要验证码");
                return data;
            }
            if(sms!=null && phone!=null && !phone.equals(user.getPhone())
                    && !sms.equals(userMapper.findSmsCode(phone))){
                data.put("result", 0);
                data.put("message", "验证码错误");
                return data;
            }
            data.put("result", 1);
            if(name!=null) user.setName(name);
            if(gender!=0) user.setGender(gender);
            if(address!=null) user.setAddress(address);
            userMapper.updateUser(user);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }


    @RequestMapping(value="/updateUserImage")
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
            userMapper.updateUserImage(user.getId(),uimage);
            // 保存成功
            user.setImage(uimage);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    /***
     * 头像下载
     */
    @RequestMapping("/download/{code}")
    public ResponseEntity<InputStreamResource> downloadFile(
            Model model,
            @PathVariable String code)
            throws IOException {
        WebConfBean wcb = (WebConfBean)model.asMap().get("confBean");
        return WebIOUtil.downloadFileWithCode(
                wcb.getProjectPath(), "image",code,
                imgPath);
    }

    /**
     * sms todo
     */
    @RequestMapping(value="/smsCode")
    public Map<String, Object> smsCode(
            @RequestParam String phone
    ) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            String code = null;
            if(userMapper.findSmsCode(phone)!=null){
                userMapper.updateSmsCode(phone,code);
            }else{
                userMapper.saveSmsCode(phone,code);
            }
//            HashMap<String, Object> result = SDKTestSendTemplateSMS.sendVerifyCode(phone, code);
//            if(!"000000".equals(result.get("statusCode"))){
//                data.put("result", 0);
//                data.put("message","验证码发送失败，请稍后再试！");
//            }
            return data;
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }


    /**
     * 忘记密码
     */
    @RequestMapping(value="/resetPassword")
    public Map<String, Object> resetPassword(
            @RequestParam String sms,
            @RequestParam String phone,
            @RequestParam String newPwd
    ) throws RestMainException {
//        User user = (User)model.asMap().get("user");
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            if(!sms.equals(userMapper.findSmsCode(phone))){
                data.put("result", 0);
                data.put("message","验证码错误");
                return data;
            }
            User user = userMapper.findUserByPhone(phone);
            if(user==null){
                data.put("result", 0);
                data.put("message","用户不存在,请注册");
                return data;
            }
            user.setPwd(CodeUtil.md5(newPwd));
            userMapper.updateUserPassword(user);
            return data;
        }catch (Exception e){
            throw new RestMainException(e);
        }
    }
}
