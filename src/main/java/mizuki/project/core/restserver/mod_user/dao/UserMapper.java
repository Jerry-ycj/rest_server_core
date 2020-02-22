package mizuki.project.core.restserver.mod_user.dao;

import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.config.mybatis.typeHandler.array.StringArrayHandler;
import mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonbHandler;
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

    @Select("select '${schema}' as schema, * from ${schema}.role where id>0 and department>=0 order by id")
    @Results(id = "role_all", value = {
            @Result(property = "privileges", column = "privileges", typeHandler = StringArrayHandler.class),
            @Result(property = "department", column = "id=department, schema=schema", one = @One(select = "mizuki.project.core.restserver.mod_user.dao.DepartmentMapper.findById")),
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
    })
    List<Role> listRoles(@Param("schema") String schema);

    @Select("select '${schema}' as schema, * from ${schema}.role where id=#{id}")
    @ResultMap("role_all")
    Role findRole(@Param("schema") String schema, @Param("id") int id);

    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT_BY_SCHEMA)
    void saveRole(@Param("schema") String schema, @Param("bean") Role role);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATE_BY_SCHEMA)
    void updateRole(@Param("schema") String schema, @Param("bean") Role role);

    @Delete("delete from ${schema}.role where id=#{param2} and id>0")
    void delRole(@Param("schema") String schema, int id);

    @Select("select * from ${schema}.privilege_constant where type<>'dev' order by sort")
    List<PrivilegeConstantBean> listPrivileges(@Param("schema") String schema);

    @Select("select * from ${schema}.role where id>0 and department=#{param2} order by id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class)
    })
    List<Role> listRoleByDepartment(@Param("schema") String schema, int id);

    // 从department的树状遍历
    @Select("select '${schema}' as schema, * from ${schema}.role where id>0 and department in (" +
            " with recursive t(id) as( values(#{param2}) union all select d.id from ${schema}.department d, t where t.id=d.parent) select id from t" +
            ") order by id")
    @ResultMap("role_all")
    List<Role> listRolesFromRootDepart(@Param("schema") String schema, int id);

    /**
     * user
     */

    @Select("select * from ${schema}.admin_user where role=#{param2} and off>-1")
    List<User> listByRole(@Param("schema") String schema, int rid);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where id=#{id}")
    @Results(id="user_all",value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "id=role, schema=schema", one = @One(select = "findRole")),
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
    })
    User findById(@Param("schema") String schema, @Param("id") int id);

    @Select("select * from ${schema}.admin_user where id=#{id}")
    User findInfoOnlyById(@Param("schema") String schema, @Param("id") int id);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where username = #{param2} and off>=0 limit 1")
    @ResultMap("user_all")
    User findUserByUsername(@Param("schema") String schema, String username);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where phone=#{param2} and off>=0 limit 1")
    @ResultMap("user_all")
    User findUserByPhone(@Param("schema") String schema, String phone);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where phone=#{param2} and pwd=#{param3} and off>=0 limit 1")
    @ResultMap("user_all")
    User loginByPhone(@Param("schema") String schema, String phone, String pwd);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where username=#{param2} and pwd=#{param3} and off>=0 limit 1")
    @ResultMap("user_all")
    User loginByUsername(@Param("schema") String schema, String username, String pwd);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where extend->>'openid'=#{param2} and off=0 limit 1")
    @ResultMap("user_all")
    User loginByOpenid(@Param("schema") String schema, String openid);

    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT_BY_SCHEMA)
    @Options(useGeneratedKeys = true, keyProperty = "bean.id")
    void saveUser(@Param("schema") String schema, @Param("bean") User user);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATE_BY_SCHEMA)
    void updateUser(@Param("schema") String schema, @Param("bean") User user);

//    @Select("select * from ${schema}.admin_user where off=0 and role>0 order by role,id")
//    @Results({
//            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
//            @Result(property = "role", column = "role", one = @One(select = "findRole"))
//    })
//    List<User> listAll(@Param("schema") String schema, );

    // 获取某一root组用户
    @Select("select '${schema}' as schema, * from ${schema}.admin_user where off>-1 and role>0 and role in(" +
            " select id from ${schema}.role where department in (" +
            "  with recursive t(id) as( values(#{param2}) union all select d.id from ${schema}.department d, t where t.id=d.parent) select id from t" +
            " )" +
            ") order by name,id")
    @ResultMap("user_all")
    List<User> listFromRootDepart(@Param("schema") String schema, int departId);

    @Select("select '${schema}' as schema, * from ${schema}.admin_user where off=0 and role>0 and role in(" +
            " select id from ${schema}.role where department in (" +
            "  with recursive t(id) as( values(#{param2}) union all select d.id from ${schema}.department d, t where t.id=d.parent) select id from t" +
            " )" +
            ") order by name,id")
    @ResultMap("user_all")
    List<User> list4SelectFromRootDepart(@Param("schema") String schema, int departId);

    /** 用户冻结等等 */
    @Update("update ${schema}.admin_user set off=#{param3} where id=#{param2}")
    void offUser(@Param("schema") String schema, int uid, int off);

    @Update("update ${schema}.admin_user set username=null, phone=null where id=#{param2}")
    void setNull(@Param("schema") String schema, int id);
}
