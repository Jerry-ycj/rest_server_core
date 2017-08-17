package mizuki.project.core.restserver.mod_user;

import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.util.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

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
public class AdminUserRestAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;

    /**
     * 管理员功能
     */
    @RequestMapping("/listUsers")
    @PreAuthorize("hasAuthority('" + Role.P_USERMNG+ "')")
    public Map listUsers(Model model) throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
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
            data.put("roles",roles);
            data.put("userRoles",user_roles);
            data.put("result",1);
            return data;
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
    }

    @RequestMapping("/addUser")
    @PreAuthorize("hasAuthority('" + Role.P_USERMNG+ "')")
    public Map<String, Object> addUser(
            Model model,
            @RequestParam String username,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String pwd,
            @RequestParam int role
    )throws RestMainException {
        Map<String,Object> data = new HashMap<>();
        try{
            if(userMapper.findUserByUsername(username)!=null){
                data.put("message","用户名已经存在");
                data.put("result",0);
                return data;
            }
            if(userMapper.findUserByPhone(phone)!=null){
                data.put("message","手机号已经存在");
                data.put("result",0);
                return data;
            }
            Role r = userMapper.findRole(role);
            if(r==null){
                data.put("message","role不存在");
                data.put("result",0);
                return data;
            }
            User user = new User().setRole(r)
                    .setName(name).setUsername(username)
                    .setPhone(phone).setPwd(CodeUtil.md5(pwd));
            userMapper.saveUser(user);
            data.put("result",1);
        }catch (Exception e){
            throw new RestMainException(e,model);
        }
        return data;
    }

    /***
     * info user
     */
    @RequestMapping("/info")
    public Map<String,Object> info(
            @RequestParam int uid
    )throws RestMainException{
        Map<String,Object> data = new HashMap<>();
        try{
            data.put("result", 1);
            User user = userMapper.findById(uid);
            if(user==null){
                data.put("result", 0);
                data.put("message","无此用户");
                return data;
            }
            data.put("user",user);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("err：",e);
            throw new RestMainException(e);
        }
    }
}
