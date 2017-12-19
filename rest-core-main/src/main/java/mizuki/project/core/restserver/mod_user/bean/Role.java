package mizuki.project.core.restserver.mod_user.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ycj on 2016/12/3.
 * 角色
 */
@Table(name = "role")
public class Role implements Serializable{
    @Transient
    public static final int ADMIN = 100; // 普通管理员
    @Transient
    public static final String P_USERMNG = "userMng";

    @Id @GeneratedValue
    private int id;
    private String name;
    private List<String> privileges;

    public List<String> getPrivileges() {
        return privileges;
    }

    public Role setPrivileges(List<String> privileges) {
        this.privileges = privileges;
        return this;
    }

    public int getId() {
        return id;
    }

    public Role setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }
}
