package mizuki.project.core.restserver.mod_user.bean.ret;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.User;

public class UserRet extends BasicRet{
    public class UserRetData{
        private User user;

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

    public UserRet setData(UserRetData data) {
        this.data = data;
        return this;
    }
}
