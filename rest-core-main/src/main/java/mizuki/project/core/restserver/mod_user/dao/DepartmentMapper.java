package mizuki.project.core.restserver.mod_user.dao;

import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonHandler;
import mizuki.project.core.restserver.mod_user.bean.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT)
    void save(Department department);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATEALL)
    void update(Department department);

    @DeleteProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_DELETE)
    void del(Department department);

    @Select("select * from department where id=#{param1}")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "parent",column = "parent",one = @One(select = "findById"))
    })
    Department findById(int id);

    @Select("select * from department where parent is null order by no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class)
    })
    List<Department> listParent();

    @Select("select * from department where parent=#{param1} order by no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "id",column = "id"),
            @Result(property = "children",column = "id",many = @Many(select = "listAllByParent"))
    })
    List<Department> listAllByParent(int id);

    @Select("select * from department order by no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonHandler.class),
            @Result(property = "id",column = "id"),
            @Result(property = "children",column = "id",many = @Many(select = "listAllByParent")),
            @Result(property = "parent",column = "parent",one = @One(select = "findById"))
    })
    List<Department> listAll();
}
