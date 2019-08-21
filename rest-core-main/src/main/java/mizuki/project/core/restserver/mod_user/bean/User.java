package mizuki.project.core.restserver.mod_user.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Table(name = "admin_user")
@ApiModel(value = "系统用户")
public class User implements Serializable{
	@Transient
	private static final long serialVersionUID = 1L;
	@Transient
	public static final int OFF_OK = 0;
	@Transient
    public static final int OFF_FREEZE = 1; // 冻结
	@Transient
	public static final int OFF_DEL = -1;	// 删除

	@Id @GeneratedValue
	private int id;
	@OneToOne
	@JoinColumn(name = "role", referencedColumnName = "id")
	private Role role;
	private String username;
	private String name;
	private String phone;
    @JSONField(serialize = false)
	private String pwd;
    @ApiModelProperty(notes = "1-male,2f-female")
	private int gender;
    @ApiModelProperty(notes = "头像地址")
	private String image;
	private String address;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createDt;
	@ApiModelProperty(notes = "1-冻结, 2-等待审核，-1-删除")
    private int off;
	@ApiModelProperty(notes = "immutable:true 不可删除, openid")
	private Map<String,Object> extend;

	public Map<String, Object> getExtend() {
		if(extend==null) extend = new HashMap<>();
		return extend;
	}

	public User setExtend(Map<String, Object> extend) {
		this.extend = extend;
		return this;
	}

	public int getId() {
		return id;
	}

	public User setId(int id) {
		this.id = id;
		return this;
	}

    public int getOff() {
		return off;
	}

	public User setOff(int off) {
		this.off = off;
		return this;
	}

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getPwd() {
		return pwd;
	}

	public User setPwd(String pwd) {
		this.pwd = pwd;
		return this;
	}

	public int getGender() {
		return gender;
	}

	public User setGender(int gender) {
		this.gender = gender;
		return this;
	}

	public String getImage() {
		return image;
	}

	public User setImage(String image) {
		this.image = image;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public User setAddress(String address) {
		this.address = address;
		return this;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public User setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
		return this;
	}
}
