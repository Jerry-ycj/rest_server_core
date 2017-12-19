package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.mod_user.bean.ret.UserRet;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
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

    @RequestMapping(value = "/listUsers",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + Role.P_USERMNG+ "')")
    @ApiOperation(value = "用户列表")
    public BasicMapDataRet listUsers(Model model) throws RestMainException {
        try{
            BasicMapDataRet ret = new BasicMapDataRet();
            List<Role> roles = userMapper.listRoles();
            // 分批role 对应 users:  list: map{role,Role}{users,list}
            List<Map<String,Object>> user_roles = new ArrayList<>();
            for (Role role:roles){
                List<User> users = userMapper.listUserByRole(role.getId());
                Map<String,Object> map = new HashMap<>();
                map.put("role",role);
                map.put("users",users);
                user_roles.add(map);
            }
            ret.getData().put("roles",roles);
            ret.getData().put("userRoles",user_roles);
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
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam int role
    )throws RestMainException {
        try{
            if(userMapper.findUserByUsername(username)!=null){
                return new BasicRet(BasicRet.ERR,"用户名已经存在");
            }
            if(userMapper.findUserByPhone(phone)!=null){
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
            @RequestParam int uid
    )throws RestMainException{
        try{
            UserRet ret = new UserRet();
            User user = userMapper.findById(uid);
            if(user==null){
                return (UserRet) ret.setResult(BasicRet.ERR).setMessage("无此用户");
            }
            ret.getData().setUser(user);
            return (UserRet)ret.setResult(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }
}
