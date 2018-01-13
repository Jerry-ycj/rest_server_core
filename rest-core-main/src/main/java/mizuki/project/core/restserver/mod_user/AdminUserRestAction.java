package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.mod_user.bean.ret.UserRet;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.modules.sms.SmsMapper;
import mizuki.project.core.restserver.util.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycj on 2016/12/16.
 *
 */
@RestController
@RequestMapping("/rest/admin/user")
@SessionAttributes({"user"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理用户模块-用户管理",description = "用户管理")
public class AdminUserRestAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SmsMapper smsMapper;

    @RequestMapping(value = "/listUsers",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + Role.P_USERMNG+ "')")
    @ApiOperation(value = "用户列表")
    public BasicMapDataRet listUsers(Model model) throws RestMainException {
        try{
            BasicMapDataRet ret = new BasicMapDataRet();
            ret.getData().put("users",userMapper.listAll());
            ret.setResult(BasicRet.SUCCESS);
            return ret;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + Role.P_USERMNG+ "')")
    @ApiOperation(value = "添加用户")
    public BasicRet addUser(
            Model model,
            @RequestParam String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam String pwd,
            @RequestParam int role
    )throws RestMainException {
        try{
            if(userMapper.findUserByUsername(username)!=null){
                return new BasicRet(BasicRet.ERR,"用户名已经存在");
            }
            if(phone!=null && userMapper.findUserByPhone(phone)!=null){
                return new BasicRet(BasicRet.ERR,"手机号已经存在");
            }
            Role r = userMapper.findRole(role);
            if(r==null){
                return new BasicRet(BasicRet.ERR,"role不存在");
            }
            User user = new User().setRole(r)
                    .setName(name).setUsername(username)
                    .setPhone(phone).setPwd(CodeUtil.md5(pwd));
            userMapper.saveUser(user);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST)
    @ApiOperation(value = "用户信息")
    public UserRet info(
            Model model,
            @ApiParam(value = "不传则表示自己")
            @RequestParam(required = false) Integer uid
    )throws RestMainException{
        try{
            User user = (User)model.asMap().get("user");
            UserRet ret = new UserRet();
            if(uid==null){
                ret.getData().setUser(user);
            }else{
                User target = userMapper.findById(uid);
                if(target==null){
                    return (UserRet) ret.setResult(BasicRet.ERR).setMessage("无此用户");
                }
                ret.getData().setUser(target);
            }
            return (UserRet)ret.setResult(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping(value="/offUser",method= RequestMethod.POST)
    @ApiOperation(value = "冻结或激活用户")
    public BasicRet offUser(
            Model model,
            @RequestParam int uid,
            @RequestParam boolean off
    ) throws RestMainException{
        try{
            User user = (User)model.asMap().get("user");
            if(user.getId()==uid){
                return new BasicRet(BasicRet.ERR,"不能设置自己");
            }
            User target = userMapper.findById(uid);
            if(off){
                userMapper.offUser(target.getId(),User.OFF_FREEZE);
            }else{
                userMapper.offUser(target.getId(),User.OFF_OK);
            }
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e, model);
        }
    }

    @RequestMapping(value="/updateUser",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息")
    public BasicRet updateUserInfo(
            Model model,
            @RequestParam int id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam(required = false)String address,
            @RequestParam(required = false)String pwd,
            @RequestParam(required = false,defaultValue = "0") int role
    ) throws RestMainException {
        try{
            User user = userMapper.findById(id);
            if(user==null){
                return new BasicRet(BasicRet.ERR,"用户不存在");
            }
            if(phone!=null && !phone.equals(user.getPhone())){
                user.setPhone(phone);
            }
            if(username!=null && !username.equals(user.getUsername())){
                if(userMapper.findUserByUsername(username)!=null){
                    return new BasicRet(BasicRet.ERR,"改用户名已被使用");
                }else{
                    user.setUsername(username);
                }
            }
            if(role>0 && role!=user.getRole().getId()){
                Role r = userMapper.findRole(role);
                if(r==null){
                    return new BasicRet(BasicRet.ERR,"role不存在");
                }
                user.setRole(r);
            }
            if(pwd!=null) user.setPwd(CodeUtil.md5(pwd));
            if(name!=null) user.setName(name);
            if(gender!=0) user.setGender(gender);
            if(address!=null) user.setAddress(address);
            userMapper.updateUser(user);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }
}
