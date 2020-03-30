package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.mod_user.bean.ret.UserListRet;
import mizuki.project.core.restserver.mod_user.bean.ret.UserRet;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.util.CodeUtil;
import mizuki.project.core.restserver.util.OtherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ycj on 2016/12/16.
 *
 */
@RestController
@RequestMapping("/rest/admin/user")
@SessionAttributes({"user","schema"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理员模块-用户管理")
public class AdminUserRestAction{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected UserCenter userCenter;


    @RequestMapping(value = "/listUsers",method = RequestMethod.POST)
    @ApiOperation(value = "用户列表-可按角色组")
//    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_LIST+ "')")
    public UserListRet listUsers(
            Model model,
            @RequestParam(required = false, defaultValue = "0") int departmentId
    ){
        UserListRet ret = new UserListRet();
        List<User> users = userMapper.listFromRootDepart(PGBaseSqlProvider.getSchema(model),departmentId);
        ret.getData().setList(users);
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }

    @RequestMapping(value="/listByRole",method={RequestMethod.POST})
    @ApiOperation(value = "根据role列表user")
    public BasicMapDataRet listByRole(
            Model model,
            @RequestParam int roleId
    ){
        BasicMapDataRet ret = new BasicMapDataRet();
        ret.getData().put("list",userMapper.listByRole(PGBaseSqlProvider.getSchema(model),roleId));
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
//    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_MNG+ "')")
    public BasicRet addUser(
            Model model,
            @RequestParam String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam String pwd,
            @RequestParam int role,
            @RequestParam(required = false) String extendJson
    )throws RestMainException {
        if(OtherUtil.isNilString(pwd)) throw new RestMainException("密码不能为空");
        if(userMapper.findUserByUsername(PGBaseSqlProvider.getSchema(model),username)!=null){
            throw new RestMainException("用户名已经存在");
        }
        if(phone!=null && userMapper.findUserByPhone(PGBaseSqlProvider.getSchema(model),phone)!=null){
            throw new RestMainException("手机号已经存在");
        }
        Role r = userMapper.findRole(PGBaseSqlProvider.getSchema(model),role);
        // role=0 不能设置
        if(r==null || role==0){
            throw new RestMainException("role不存在");
        }
        User user = new User().setRole(r)
                .setCreateDt(new Timestamp(System.currentTimeMillis()))
                .setGender(gender)
                .setName(name).setUsername(username)
                .setPhone(phone).setPwd(CodeUtil.md5(pwd));
        var extend = OtherUtil.getExtendJson(extendJson);
        if(extend!=null) user.setExtend(extend);
        userMapper.saveUser(PGBaseSqlProvider.getSchema(model),user);
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST)
    @ApiOperation(value = "用户信息")
//    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_LIST+ "')")
    public UserRet info(
            Model model,
            @ApiParam(value = "不传则表示自己")
            @RequestParam(required = false) Integer uid
    ){
        User user = (User)model.asMap().get("user");
        UserRet ret = new UserRet();
        if(uid==null){
            ret.getData().setUser(user);
        }else{
            User target = userMapper.findById(PGBaseSqlProvider.getSchema(model),uid);
            if(target==null){
                return (UserRet) ret.setResult(BasicRet.ERR).setMessage("无此用户");
            }
            if(target.getRole().getId()==0){
                return (UserRet) ret.setResult(BasicRet.ERR).setMessage("该用户不能设置");
            }
            ret.getData().setUser(target);
        }
        return (UserRet)ret.setResult(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/offUser",method= RequestMethod.POST)
    @ApiOperation(value = "冻结或激活用户")
//    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_MNG+ "')")
    public BasicRet offUser(
            Model model,
            @RequestParam int uid,
            @RequestParam boolean off
    ) throws RestMainException{
        return del(model, uid, off?1:0);
    }
    @RequestMapping(value="/del",method= RequestMethod.POST)
    @ApiOperation(value = "删除冻结用户")
    public BasicRet del(
            Model model,
            @RequestParam int id,
            @ApiParam(value = "0-删除，1-冻结，2-解冻")
            @RequestParam int off
    ) throws RestMainException{
        User user = (User)model.asMap().get("user");
        if(user.getId()==id){
            throw new RestMainException("不能设置自己");
        }
        User target = userMapper.findById(PGBaseSqlProvider.getSchema(model),id);
        if(target==null) throw new RestMainException("用户不存在");
        if(target.getRole().getId()==0){
            throw new RestMainException("该用户不能设置");
        }
        if(off==0){
            userMapper.offUser(PGBaseSqlProvider.getSchema(model),id, User.OFF_DEL);
            userMapper.setNull(PGBaseSqlProvider.getSchema(model),id);
            target.setOff(User.OFF_DEL);
            userCenter.add(target);
        }else if(off==1){
            userMapper.offUser(PGBaseSqlProvider.getSchema(model),id, User.OFF_FREEZE);
            target.setOff(User.OFF_FREEZE);
            userCenter.add(target);
        }else if(off==2){
            userMapper.offUser(PGBaseSqlProvider.getSchema(model),id, User.OFF_OK);
            target.setOff(User.OFF_OK);
            userCenter.add(target);
        }
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/updateUser",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息")
//    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_MNG+ "')")
    public BasicRet updateUserInfo(
            Model model,
            @RequestParam int id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false,defaultValue = "0")int gender,
            @RequestParam(required = false)String address,
            @RequestParam(required = false)String pwd,
            @RequestParam(required = false,defaultValue = "0") int role,
            @RequestParam(required = false)String extendJson
    ) throws RestMainException {
        User user = userMapper.findById(PGBaseSqlProvider.getSchema(model),id);
        if(user==null){
            throw new RestMainException("用户不存在");
        }
        if(user.getRole().getId()==0){
            throw new RestMainException("该用户不能设置");
        }
        if(username!=null && !username.equals(user.getUsername())){
            if(userMapper.findUserByUsername(PGBaseSqlProvider.getSchema(model),username)!=null){
                throw new RestMainException("该用户名已被使用");
            }else{
                user.setUsername(username);
            }
        }
        if(phone!=null && !"".equals(phone.trim()) && !phone.equals(user.getPhone()) && userMapper.findUserByPhone(PGBaseSqlProvider.getSchema(model),phone)!=null){
            throw new RestMainException("手机号已经存在");
        }
        if(role>0 && role!=user.getRole().getId()){
            Role r = userMapper.findRole(PGBaseSqlProvider.getSchema(model),role);
            if(r==null){
                throw new RestMainException("role不存在");
            }
            user.setRole(r);
        }
        if(phone!=null && !phone.equals(user.getPhone())){
            user.setPhone(phone);
        }
        if(!OtherUtil.isNilString(pwd)) user.setPwd(CodeUtil.md5(pwd));
        if(!OtherUtil.isNilString(name)) user.setName(name.trim());
        if(gender!=0) user.setGender(gender);
        if(!OtherUtil.isNilString(address)) user.setAddress(address);
        var extend = OtherUtil.getExtendJson(extendJson);
        if(extend!=null) user.getExtend().putAll(extend);
        userMapper.updateUser(PGBaseSqlProvider.getSchema(model),user);
        userCenter.add(user);
        return new BasicRet(BasicRet.SUCCESS);
    }
}
