package mizuki.project.core.restserver.mod_user.bean;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycj on 2016/12/3.
 * 角色
 */
@Table(name = "role")
@ApiModel(value = "角色")
public class Role implements Serializable{
    @Transient
    private static final long serialVersionUID = 1L;

    // 上帝角色
    @Transient
    public static final int DEFAULT_ALL = 0;

    @Id @GeneratedValue
    private int id;
    private String name;
    private String description;
    private List<String> privileges;
    @OneToOne
    @JoinColumn(name = "department", referencedColumnName = "id")
    private Department department;
    private Map<String,Object> extend;

    public Map<String, Object> getExtend() {
        if(extend==null) extend = new HashMap<>();
        return extend;
    }

    public Role setExtend(Map<String, Object> extend) {
        this.extend = extend;
        return this;
    }

    public Department getDepartment() {
        return department;
    }

    public Role setDepartment(Department department) {
        this.department = department;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Role setDescription(String description) {
        this.description = description;
        return this;
    }

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
