package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.mod_user.bean.ret.DepartmentListRet;
import mizuki.project.core.restserver.mod_user.bean.ret.LoginUserRet;
import mizuki.project.core.restserver.mod_user.bean.ret.RoleListRet;
import mizuki.project.core.restserver.mod_user.dao.DepartmentMapper;
import mizuki.project.core.restserver.mod_user.dao.SmsMapper;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.modules.session.SpringSessionService;
import mizuki.project.core.restserver.util.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/rest/user")
@SessionAttributes({"user","sessionId"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "系统用户模块")
public class UserRestAction{

	@Autowired
	protected UserMapper userMapper;
	@Autowired
    protected SmsMapper smsMapper;
	@Autowired
    protected DepartmentMapper departmentMapper;
    @Autowired
    protected SpringSessionService sessionService;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected User latestUser(Model model){
        User user = (User)model.asMap().get("user");
        user = userMapper.findById(user.getId());
        model.addAttribute("user",user);
        return user;
    }

    @RequestMapping(value = "/listRoles",method = RequestMethod.POST)
    @ApiOperation(value = "获取角色列表")
    public RoleListRet listRoles(){
        RoleListRet ret = new RoleListRet();
        ret.getData().setRoles(userMapper.listRoles());
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }

    @RequestMapping(value="/listDepartment",method= RequestMethod.POST)
    @ApiOperation(value = "获取部门列表")
    public DepartmentListRet listDepartment(){
        DepartmentListRet ret = new DepartmentListRet();
        ret.getData().setDepartments(departmentMapper.listAll());
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ApiOperation(value = "登出")
    public BasicRet logout(
            Model model,
            HttpSession session
    ){
        model.asMap().remove("user");
        session.removeAttribute("user");
        sessionService.checkAndUpdateSession(session,model,"user");
        return new BasicRet(BasicRet.SUCCESS);
    }

	@RequestMapping(value="/loginByPhone",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录（手机）")
	public LoginUserRet userLogin(
            @RequestParam String phone,
            @RequestParam String pwd,
            Model model
    ) throws RestMainException {
        LoginUserRet ret=new LoginUserRet();
        phone = phone.trim();
        String passwd = CodeUtil.md5(pwd);
        User user = userMapper.loginByPhone(phone,passwd);
        return loginHandle(user,ret,model);
	}

    @RequestMapping(value = "/loginByUsername",method = RequestMethod.POST)
    @ApiOperation(value = "用户名登录")
    public LoginUserRet login(
            Model model,
            @RequestParam String username,
            @RequestParam String pwd
    ) throws RestMainException{
        LoginUserRet ret=new LoginUserRet();
        username = username.trim();
        String passwd = CodeUtil.md5(pwd);
        User user = userMapper.loginByUsername(username,passwd);
        return loginHandle(user,ret,model);
    }

//    @RequestMapping(value = "/loginWithWxCode",method = RequestMethod.POST)
//    @ApiOperation(value = "登录-绑定微信小程序code")
//    public LoginUserRet loginWithWxMini(
//            Model model,
//            @RequestParam String username,
//            @RequestParam String pwd,
//            @RequestParam String code
//    ) throws RestMainException{
//        LoginUserRet ret=new LoginUserRet();
//        username = username.trim();
//        String passwd = CodeUtil.md5(pwd);
//        User user = userMapper.loginByUsername(username,passwd);
//        return loginHandle(user,ret,model);
//    }

//    @RequestMapping(value = "/loginByWxCode",method = RequestMethod.POST)
//    @ApiOperation(value = "微信openid登录")
//    public LoginUserRet loginByWxCode(
//            Model model,
//            @RequestParam String code
//    ) throws RestMainException{
//        LoginUserRet ret=new LoginUserRet();
//        User user = userMapper.
//        return loginHandle(user,ret,model);
//    }

    @RequestMapping(value="/loginSms",method = RequestMethod.POST)
    @ApiOperation(value = "短信登录")
    public LoginUserRet userLoginSms(
            @RequestParam String phone,
            @RequestParam String sms,
            Model model
    ) throws RestMainException {
        LoginUserRet ret=new LoginUserRet();
        phone = phone.trim();
        if(!sms.equals(smsMapper.findSmsCode(phone))){
            throw new RestMainException("验证码错误");
        }
        User user = userMapper.findUserByPhone(phone);
        return loginHandle(user,ret,model);
    }

//    protected LoginUserRet loginHandle4Wx(User user,LoginUserRet ret,Model model,String code) throws RestMainException{
//        loginHandle(user, ret, model);
//        if(ret.getResult()==BasicRet.SUCCESS){
//            // todo
//            if(wxMiniService==null) throw new RestMainException("wx mini service error");
//        }
//        return ret;
//    }

    /***
     * 登录时  获取user 和 systems
     */
    protected LoginUserRet loginHandle(User user,LoginUserRet ret,Model model) throws RestMainException {
        if(user==null)
            throw new RestMainException("用户名或密码错误");
        if(user.getOff()==User.OFF_FREEZE){
            throw new RestMainException("账户被冻结");
        }
//        else if(user.getOff()==2){
//            throw new RestMainException("账户审核中");
//        }
        String token = (String) model.asMap().get("sessionId");
//        if(userMapper.findRestToken(user.getId())==null){
//            userMapper.saveRestToken(user.getId(),token);
//        }else {
//            userMapper.updateRestToken(user.getId(), token);
//        }
        model.addAttribute("user",user);
        ret.setToken(token);
        ret.setUser(user);
        ret.getData().setUser(user);
        ret.getData().setToken(token);
        return (LoginUserRet) ret.setResult(BasicRet.SUCCESS);
    }


    @RequestMapping(value="/updatePwd",method = RequestMethod.POST)
    @ApiOperation(value = "密码修改")
    public BasicRet updateUserPassword(
            Model model,
            HttpSession session,
            @RequestParam String oldPwd,
            @RequestParam String newPwd
    ) throws RestMainException {
        User user = latestUser(model);
        if(!user.getPwd().equals(CodeUtil.md5(oldPwd))){
            throw new RestMainException("密码错误");
        }
        user.setPwd(CodeUtil.md5(newPwd));
        userMapper.updateUser(user);
        sessionService.checkAndUpdateSession(session,model,"user");
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/updateUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息")
    public BasicRet updateUserInfo(
            Model model,
            HttpSession session,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam(required = false)String address,
            @RequestParam(required = false)String sms
    ) throws RestMainException {
        User user = (User)model.asMap().get("user");
        if(phone!=null && !phone.equals(user.getPhone()) && sms==null){
            throw new RestMainException("修改手机需要验证码");
        }
        if(sms!=null && phone!=null && !phone.equals(user.getPhone())
                && !sms.equals(smsMapper.findSmsCode(phone))){
            throw new RestMainException("验证码错误");
        }
        if(name!=null){
            name = name.trim();
            user.setName(name);
        }
        if(phone!=null && !"".equals(phone.trim())){
            user.setPhone(phone.trim());
        }
        if(gender!=0) user.setGender(gender);
        if(address!=null) user.setAddress(address);
        userMapper.updateUser(user);
        sessionService.checkAndUpdateSession(session,model,"user");
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/resetPassword",method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public BasicRet resetPassword(
            @RequestParam String sms,
            @RequestParam String phone,
            @RequestParam String newPwd
    ) throws RestMainException {
        if(!sms.equals(smsMapper.findSmsCode(phone))){
            throw new RestMainException("验证码错误");
        }
        User user = userMapper.findUserByPhone(phone);
        if(user==null){
            throw new RestMainException("用户不存在");
        }
        user.setPwd(CodeUtil.md5(newPwd));
        userMapper.updateUser(user);
        return new BasicRet(BasicRet.SUCCESS);
    }
}
