package mizuki.project.core.restserver.mod_user.bean.ret;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.User;

import java.util.HashMap;
import java.util.Map;


public class LoginUserRet extends BasicRet{
    // todo 不合规范、兼容
    private User user;
    private String token;
    public class UserRetData{
        private User user;
        private String token;

        public String getToken() {
            return token;
        }

        public UserRetData setToken(String token) {
            this.token = token;
            return this;
        }

        public User getUser() {
            return user;
        }

        public UserRetData setUser(User user) {
            this.user = user;
            return this;
        }
    }
    private UserRetData data = new UserRetData();

    public UserRetData getData() {
        return data;
    }

    public LoginUserRet setData(UserRetData data) {
        this.data = data;
        return this;
    }

    public User getUser() {
        return user;
    }

    public LoginUserRet setUser(User user) {
        this.user = user;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginUserRet setToken(String token) {
        this.token = token;
        return this;
    }
}
