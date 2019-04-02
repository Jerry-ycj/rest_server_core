package mizuki.project.core.restserver.mod_user.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;

@Table(name = "department")
@ApiModel(value = "系统用户")
public class Department {
    @Id
    @GeneratedValue
    private int id;
    private String no;
    private String name;
    private String descr;
    @OneToOne
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private Department parent;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDt;
    private Map<String,Object> extend;

    public int getId() {
        return id;
    }

    public Department setId(int id) {
        this.id = id;
        return this;
    }

    public String getNo() {
        return no;
    }

    public Department setNo(String no) {
        this.no = no;
        return this;
    }

    public String getName() {
        return name;
    }

    public Department setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescr() {
        return descr;
    }

    public Department setDescr(String descr) {
        this.descr = descr;
        return this;
    }

    public Department getParent() {
        return parent;
    }

    public Department setParent(Department parent) {
        this.parent = parent;
        return this;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public Department setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
        return this;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public Department setExtend(Map<String, Object> extend) {
        this.extend = extend;
        return this;
    }
}
