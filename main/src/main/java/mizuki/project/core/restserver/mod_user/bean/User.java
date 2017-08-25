package mizuki.project.core.restserver.mod_user.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import mizuki.project.core.restserver.util.CodeUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable{

	public static final int OFF_OK = 0;
    public static final int OFF_FREEZE = 1; // 冻结
    public static final int OFF_CHECK = 2;  // 审核中

	private int id;
	private Role role;
	private String username;
	private String name;
	private String phone;
    @JsonIgnore
	private String pwd;
	private int gender;	// 1-nan,2-nv
	private String image;// 头像
	private String address;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createDt;
    private int off;    // 1-冻结, 2-等待审核
	// 头像地址编码
	private String imageCode;

    public String getImageCode() {
        return CodeUtil.base64UrlEncode(image);
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
