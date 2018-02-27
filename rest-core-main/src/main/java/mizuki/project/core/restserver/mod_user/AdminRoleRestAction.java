package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin/user/role")
@SessionAttributes({"user"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理用户模块-角色管理",description = "角色管理")
@PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.USER_MNG+ "')")
public class AdminRoleRestAction {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value="/listAllPrivileges",method= RequestMethod.POST)
    @ApiOperation(value = "所有权限")
    public BasicMapDataRet listAllPrivileges(Model model) throws RestMainException{
        try{
            BasicMapDataRet ret = new BasicMapDataRet();
            ret.getData().put("privileges",userMapper.findRole(Role.DEFAULT_ALL).getPrivileges());
            ret.setResult(BasicRet.SUCCESS);
            return ret;
        }catch (Exception e){
            throw new RestMainException(e, model);
        }
    }

    @RequestMapping(value="/create",method= RequestMethod.POST)
    @ApiOperation(value = "")
    public BasicRet create(
            Model model,
            @RequestParam String name,
            @ApiParam(required = true,value = "数组json字符串：[a,b,c]")
            @RequestParam String privilegesJson
    ) throws RestMainException {
        try{
            List<String> privileges = (List<String>) JsonUtil.toObject(privilegesJson,List.class);
            if(privileges==null) return new BasicRet(BasicRet.ERR,"权限不能为空");
            Role role = new Role().setName(name).setPrivileges(privileges);
            userMapper.saveRole(role);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e, model);
        }
    }

    @RequestMapping(value="/update",method= RequestMethod.POST)
    @ApiOperation(value = "")
    public BasicRet update(
            Model model,
            @RequestParam int id,
            @RequestParam String name,
            @ApiParam(required = true,value = "数组json字符串：[a,b,c]")
            @RequestParam String privilegesJson
    ) throws RestMainException {
        try{
            List<String> privileges = (List<String>) JsonUtil.toObject(privilegesJson,List.class);
            if(privileges==null) return new BasicRet(BasicRet.ERR,"权限不能为空");
            Role role = userMapper.findRole(id);
            if(role==null) return new BasicRet(BasicRet.ERR,"角色不存在");
            role.setName(name).setPrivileges(privileges);
            userMapper.updateRole(role);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e, model);
        }
    }

    @RequestMapping(value="/del",method= RequestMethod.POST)
    @ApiOperation(value = "删除角色")
    public BasicRet del(
            Model model,
            @RequestParam int id
    ) throws RestMainException{
        try{
            Role role = userMapper.findRole(id);
            if(role==null) return new BasicRet(BasicRet.ERR,"角色不存在");
            if(userMapper.listByRole(id).size()>0) return new BasicRet(BasicRet.ERR,"角色下还有用户,不能删除");
            userMapper.delRole(id);
            return new BasicRet(BasicRet.SUCCESS);
        }catch (Exception e){
            throw new RestMainException(e, model);
        }
    }
}
