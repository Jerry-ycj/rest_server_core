package mizuki.project.core.restserver.mod_user.bean;

import javax.persistence.Table;

@Table(name = "privilege_constant")
public class PrivilegeConstantBean {
    private String id ;
    private String name;
    private String type;
    private int sort;

    public String getId() {
        return id;
    }

    public PrivilegeConstantBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PrivilegeConstantBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public PrivilegeConstantBean setType(String type) {
        this.type = type;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public PrivilegeConstantBean setSort(int sort) {
        this.sort = sort;
        return this;
    }
}
