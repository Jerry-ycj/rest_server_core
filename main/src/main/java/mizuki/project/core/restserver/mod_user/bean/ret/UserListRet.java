package mizuki.project.core.restserver.mod_user.bean.ret;

import io.swagger.annotations.ApiModelProperty;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.Role;

import java.util.List;
import java.util.Map;

public class UserListRet extends BasicRet{
    private UserListRetData data = new UserListRetData();
    public class UserListRetData{
        private List<Role> roles;
        @ApiModelProperty(notes = "role和user对应关系: [{role:{Role}, users:[{User}]}]")
        private List<Map<String,Object>> userRoles;

        public List<Map<String, Object>> getUserRoles() {
            return userRoles;
        }

        public UserListRetData setUserRoles(List<Map<String, Object>> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public UserListRetData setRoles(List<Role> roles) {
            this.roles = roles;
            return this;
        }
    }

    public UserListRetData getData() {
        return data;
    }

    public UserListRet setData(UserListRetData data) {
        this.data = data;
        return this;
    }
}
