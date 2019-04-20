package mizuki.project.core.restserver.mod_user.bean.ret;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.mod_user.bean.Department;

import java.util.List;

public class DepartmentListRet extends BasicRet{
    public class DepartmentListRetData{
        private List<Department> departments;

        public List<Department> getDepartments() {
            return departments;
        }

        public DepartmentListRetData setDepartments(List<Department> departments) {
            this.departments = departments;
            return this;
        }
    }
    private DepartmentListRetData data = new DepartmentListRetData();

    public DepartmentListRetData getData() {
        return data;
    }

    public DepartmentListRet setData(DepartmentListRetData data) {
        this.data = data;
        return this;
    }
}
