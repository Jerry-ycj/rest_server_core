package mizuki.project.core.restserver.mod_user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.mod_user.bean.Department;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.dao.DepartmentMapper;
import mizuki.project.core.restserver.mod_user.dao.UserMapper;
import mizuki.project.core.restserver.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/user/role")
@SessionAttributes({"user"})
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理员模块-角色管理")
public class AdminRoleRestAction {
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected DepartmentMapper departmentMapper;

    @RequestMapping(value="/listAllPrivileges",method= RequestMethod.POST)
    @ApiOperation(value = "所有权限")
    public BasicMapDataRet listAllPrivileges(){
        BasicMapDataRet ret = new BasicMapDataRet();
        ret.getData().put("privileges",userMapper.listPrivileges(PGBaseSqlProvider.SCHEMA));
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }

    @RequestMapping(value="/create",method= RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.ROLE_MNG+ "')")
    public BasicRet create(
            @RequestParam String name,
            @ApiParam(required = true,value = "数组json字符串：[a,b,c]")
            @RequestParam String privilegesJson,
            @RequestParam(required = false, defaultValue = "0") int departmentId
    ) throws RestMainException {
        List<String> privileges = (List<String>) JsonUtil.toObject(privilegesJson,List.class);
        if(privileges==null) throw new RestMainException("权限不能为空");
        Role role = new Role().setName(name).setPrivileges(privileges);
        Department department = departmentMapper.findById(PGBaseSqlProvider.SCHEMA,departmentId);
        if(department==null) throw new RestMainException("部门不存在");
        role.setDepartment(department);
        userMapper.saveRole(PGBaseSqlProvider.SCHEMA,role);
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/update",method= RequestMethod.POST)
    @ApiOperation(value = "修改角色")
    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.ROLE_MNG+ "')")
    public BasicRet update(
            @RequestParam int id,
            @RequestParam String name,
            @ApiParam(required = true,value = "数组json字符串：[a,b,c]")
            @RequestParam String privilegesJson,
            @RequestParam(required = false, defaultValue = "0") int departmentId
    ) throws RestMainException {
        List<String> privileges = (List<String>) JsonUtil.toObject(privilegesJson,List.class);
        if(privileges==null) throw new RestMainException("权限不能为空");
        Role role = userMapper.findRole(PGBaseSqlProvider.SCHEMA, id);
        if(role==null) throw new RestMainException("角色不存在");
        role.setName(name).setPrivileges(privileges);
        if(departmentId!=role.getDepartment().getId()){
            Department department = departmentMapper.findById(PGBaseSqlProvider.SCHEMA, departmentId);
            if(department==null) throw new RestMainException("部门不存在");
            role.setDepartment(department);
        }
        userMapper.updateRole(PGBaseSqlProvider.SCHEMA, role);
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/del",method= RequestMethod.POST)
    @ApiOperation(value = "删除角色")
    @PreAuthorize("hasAuthority('" + PrivilegeConstantDefault.ROLE_MNG+ "')")
    public BasicRet del(
            @RequestParam int id
    ) throws RestMainException {
        Role role = userMapper.findRole(PGBaseSqlProvider.SCHEMA, id);
        if(role==null) throw new RestMainException("角色不存在");
        if((boolean)role.getExtend().getOrDefault("immutable",false)){
            throw new RestMainException("该角色不可删除");
        }
        if(userMapper.listByRole(PGBaseSqlProvider.SCHEMA, id).size()>0) throw new RestMainException("角色下还有用户,不能删除");
        userMapper.delRole(PGBaseSqlProvider.SCHEMA, id);
        return new BasicRet(BasicRet.SUCCESS);
    }


    @RequestMapping(value="/department/create",method= RequestMethod.POST)
    @ApiOperation(value = "创建部门")
    public BasicRet createDepartment(
            @RequestParam(required = false) String no,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer parentId
    ) throws RestMainException{
        Department department = new Department()
                .setCreateDt(new Timestamp(System.currentTimeMillis()))
                .setDescr(description).setName(name).setNo(no);
        if(parentId!=null){
            Department parent = departmentMapper.findById(PGBaseSqlProvider.SCHEMA,parentId);
            if(parent==null) throw new RestMainException("父级部门不存在");
            department.setParent(parent);
        }
        departmentMapper.save(PGBaseSqlProvider.SCHEMA,department);
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/department/update",method= RequestMethod.POST)
    @ApiOperation(value = "修改部门")
    public BasicRet updateDepartment(
            @RequestParam int id,
            @RequestParam(required = false) String no,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer parentId
    ) throws RestMainException{
        Department department = departmentMapper.findById(PGBaseSqlProvider.SCHEMA,id);
        if(department==null) throw new RestMainException("部门不存在");
        if(no!=null) department.setNo(no);
        if(name!=null) department.setName(name);
        if(description!=null) department.setDescr(description);
        if(parentId==null){
            department.setParent(null);
        }else if(department.getParent()==null || !parentId.equals(department.getParent().getId())){
            Department parent = departmentMapper.findById(PGBaseSqlProvider.SCHEMA,parentId);
            if(parent==null) throw new RestMainException("父级部门不存在");
            department.setParent(parent);
        }
        departmentMapper.update(PGBaseSqlProvider.SCHEMA,department);
        return new BasicRet(BasicRet.SUCCESS);
    }

    @RequestMapping(value="/department/del",method= RequestMethod.POST)
    @ApiOperation(value = "删除部门")
    public BasicRet delDepartment(
            @RequestParam int id
    ) throws RestMainException{
        if(userMapper.listRoleByDepartment(PGBaseSqlProvider.SCHEMA,id).size()>0) throw new RestMainException("部门下还有角色,不能删除");
        Department department = departmentMapper.findById(PGBaseSqlProvider.SCHEMA,id);
        if((boolean)department.getExtend().getOrDefault("immutable",false)){
            throw new RestMainException("该部门不可删除");
        }
        departmentMapper.del(PGBaseSqlProvider.SCHEMA,new Department().setId(id));
        return new BasicRet(BasicRet.SUCCESS);
    }
}
