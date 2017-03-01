package mizuki.project.core.restserver.modules.weixin;

/**
 * Created by ycj on 16/9/4.
 *
 */
public class WxUser {

    private String openid;
    private boolean subscribe;
    private String nickname;
    private int sex;
    private String headImgUrl;
    private String unionId;
    private int groupId;
    private String name;
    private String phone;

    public String getOpenid() {
        return openid;
    }

    public WxUser setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getName() {
        return name;
    }

    public WxUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public WxUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public WxUser setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public WxUser setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public WxUser setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public WxUser setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public String getUnionId() {
        return unionId;
    }

    public WxUser setUnionId(String unionId) {
        this.unionId = unionId;
        return this;
    }

    public int getGroupId() {
        return groupId;
    }

    public WxUser setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }
}
