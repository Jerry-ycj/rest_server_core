package mizuki.project.core.restserver.config.mybatis.provider;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    @JoinColumn(name = "role2", referencedColumnName = "id2")
    private Test role;
    private int num;
    @Column(name = "text")
    private String name;
    private Map map;
    private List<String> list;
    private Timestamp dt;
    @Transient
    private String imageCode;

    public List<String> getList() {
        return list;
    }

    public Test setList(List<String> list) {
        this.list = list;
        return this;
    }

    public int getId() {
        return id;
    }

    public Test setId(int id) {
        this.id = id;
        return this;
    }

    public Test getRole() {
        return role;
    }

    public Test setRole(Test role) {
        this.role = role;
        return this;
    }

    public int getNum() {
        return num;
    }

    public Test setNum(int num) {
        this.num = num;
        return this;
    }

    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }

    public Map getMap() {
        return map;
    }

    public Test setMap(Map map) {
        this.map = map;
        return this;
    }

    public Timestamp getDt() {
        return dt;
    }

    public Test setDt(Timestamp dt) {
        this.dt = dt;
        return this;
    }

    public String getImageCode() {
        return imageCode;
    }

    public Test setImageCode(String imageCode) {
        this.imageCode = imageCode;
        return this;
    }
}
