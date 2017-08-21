package mizuki.project.core.restserver.mod_user;

import mizuki.project.core.restserver.config.mybatis.typeHandler.StringArrayHandler;
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

    @Select("select * from role")
    @Results(
            @Result(property = "privileges",column = "privileges",typeHandler = StringArrayHandler.class)
    )
    List<Role> listRoles();

    @Select("select * from role where id=#{param1}")
    @Results({
            @Result(property = "privileges", column = "privileges", typeHandler = StringArrayHandler.class)
    })
    Role findRole(int id);

    /**
     * user
     */

    @Select("select * from user_ where id=#{param1}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User findById(int id);

    @Select("select * from user_ where id=#{param1}")
    User findInfoOnlyById(int id);

    @Select("select * from user_ where username = #{param1}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User findUserByUsername(String username);

    @Select("select * from user_ where phone=#{param1}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User findUserByPhone(String phone);

    @Select("select * from user_ where phone=#{param1} and pwd=#{param2}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User loginByPhone(String phone, String pwd);

    @Select("select * from user_ where username=#{param1} and pwd=#{param2}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User loginByUsername(String username, String pwd);


    @Insert("insert into user_(pwd,phone,name,username,createDt,role) " +
            "values(#{pwd},#{phone},#{name},#{username},now(),#{role.id})")
    @Options(useGeneratedKeys = true)
    void saveUser(User user);

    @Update("update user_ set pwd=#{pwd} where id=#{id}")
    void updateUserPassword(User user);

    @Update("update user_ set name=#{name},phone=#{phone}," +
            "gender=#{gender},address=#{address} where id=#{id}")
    void updateUser(User user);

    @Update("update user_ set image=#{param2} where id=#{param1}")
    void updateUserImage(int uid, String image);

    @Select("select * from user_ where role=#{param1}")
    List<User> listUserByRole(int role);

    /** 用户冻结等等 */
    @Update("update user_ set off=#{param2} where id=#{param1}")
    void offUser(int uid, int off);


    /***
     * rest token
     */

    @Insert({"insert into rest_token(uid,token,createDt) values(#{param1},#{param2},now())"})
    void saveRestToken(int uid, String token);

    @Update("update rest_token set token=#{param2},createDt=now() where uid=#{param1}")
    void updateRestToken(int uid, String token);

    @Select("select token from rest_token where uid=#{param1}")
    String findRestToken(int uid);

    @Select("select u.* from user_ u,rest_token t where t.uid=u.id and t.token=#{param1} and now()<=t.createDt+'${hours} hour'")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "role", column = "role", one = @One(select = "findRole"))
    })
    User findUserByToken(String token, @Param("hours") int hours);

    /**
     * 验证码 sms_code
     */

    @Insert("insert into sms_code(phone,code) values(#{param1},#{param2})")
    void saveSmsCode(String phone, String code);

    @Update("update sms_code set code=#{param2} where phone=#{param1}")
    void updateSmsCode(String phone, String code);

    @Select("select code from sms_code where phone=#{param1}")
    String findSmsCode(String phone);

}
