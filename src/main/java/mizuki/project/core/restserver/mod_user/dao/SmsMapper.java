package mizuki.project.core.restserver.mod_user.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SmsMapper {
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
