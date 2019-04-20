package mizuki.project.core.restserver.mod_user.dao;

import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.config.mybatis.typeHandler.array.StringArrayHandler;
import mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonHandler;
import mizuki.project.core.restserver.mod_user.bean.PrivilegeConstantBean;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ycj on 2016/11/14.
 *
 */
@Mapper
public interface UserMapper {

    /**
     * role
     */

    @Select("select * from role where id>0 order by id")
    @Results({
            @Result(property = "privileges", column = "privileges", typeHandler = StringArrayHandler.class),
            @Result(property = "department", column = "department", one = @One(select = "mizuki.project.core.restserver.mod_user.dao.DepartmentMapper.findById"))
    })
    List<Role> listRoles();

    @Select("select * from role where id=#{param1}")
    @Results({
            @Result(property = "privileges", column = "privileges", typeHandler = StringArrayHandler.class),
            @Result(property = "department", column = "department", one = @One(select = "mizuki.project.core.restserver.mod_user.dao.DepartmentMapper.findById"))
    })
    Role findRole(int id);

    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT)
    void saveRole(Role role);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATEALL)
    void updateRole(Role role);

    @Delete("delete from role where id=#{param1} and id>0")
    void delRole(int id);

    @Select("select * from privilege_constant where type<>'dev' order by sort")
    List<PrivilegeConstantBean> listPrivileges();

    @Select("select * from role where department=#{param1} order by id")
    List<Role> listRoleByDepartment(int id);

    /**
     * user
     */

    @Select("select * from admin_user where role=#{param1}")
    List<User> listByRole(int rid);

    @Select("select * from admin_user where id=#{param1}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole")),
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
    })
    User findById(int id);

    @Select("select * from admin_user where id=#{param1}")
    User findInfoOnlyById(int id);

    @Select("select * from admin_user where username = #{param1} and off=0 limit 1")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole")),
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
    })
    User findUserByUsername(String username);

    @Select("select * from admin_user where phone=#{param1} and off=0 limit 1")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole")),
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
    })
    User findUserByPhone(String phone);

    @Select("select * from admin_user where phone=#{param1} and pwd=#{param2} and off=0 limit 1")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User loginByPhone(String phone, String pwd);

    @Select("select * from admin_user where username=#{param1} and pwd=#{param2} and off=0 limit 1")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User loginByUsername(String username, String pwd);

    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT)
    @Options(useGeneratedKeys = true)
    void saveUser(User user);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATEALL)
    void updateUser(User user);

    @Select("select * from admin_user where off=0 and role>0 order by role,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    List<User> listAll();

    @Select("select * from admin_user user, role where user.role=role.id and user.off=0 and user.role>0 and role.department is null order by role,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    List<User> listSys();

    /** 用户冻结等等 */
    @Update("update admin_user set off=#{param2} where id=#{param1}")
    void offUser(int uid, int off);
}
