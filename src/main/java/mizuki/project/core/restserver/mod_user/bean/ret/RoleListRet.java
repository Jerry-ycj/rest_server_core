package mizuki.project.core.restserver.mod_user.bean.ret;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.Role;
import mizuki.project.core.restserver.mod_user.bean.User;

import java.util.List;

public class RoleListRet extends BasicRet{
    public class RoleListRetData{
        private List<Role> roles;

        public List<Role> getRoles() {
            return roles;
        }

        public RoleListRetData setRoles(List<Role> roles) {
            this.roles = roles;
            return this;
        }
    }
    private RoleListRetData data = new RoleListRetData();

    public RoleListRetData getData() {
        return data;
    }

    public RoleListRet setData(RoleListRetData data) {
        this.data = data;
        return this;
    }
}
