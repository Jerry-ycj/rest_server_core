package mizuki.project.core.restserver.mod_user.bean.ret;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.User;

import java.util.List;

public class UserListRet extends BasicRet{
    public class UserListRetData{
        private List<User> list;

        public List<User> getList() {
            return list;
        }

        public UserListRetData setList(List<User> list) {
            this.list = list;
            return this;
        }
    }
    private UserListRetData data = new UserListRetData();

    public UserListRetData getData() {
        return data;
    }

    public UserListRet setData(UserListRetData data) {
        this.data = data;
        return this;
    }
}
