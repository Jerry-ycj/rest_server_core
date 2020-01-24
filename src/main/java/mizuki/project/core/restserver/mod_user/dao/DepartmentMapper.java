package mizuki.project.core.restserver.mod_user.dao;

import mizuki.project.core.restserver.config.mybatis.provider.PGBaseSqlProvider;
import mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonbHandler;
import mizuki.project.core.restserver.mod_user.bean.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @InsertProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_INSERT_BY_SCHEMA)
    void save(String schema, Department department);

    @UpdateProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_UPDATE_BY_SCHEMA)
    void update(String schema, Department department);

    @DeleteProvider(type = PGBaseSqlProvider.class,method = PGBaseSqlProvider.METHOD_DELETE_BY_SCHEMA)
    void del(String schema, Department department);

    @Select("select '${schema}' as schema , * from ${schema}.department where id=#{id}")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
            @Result(property = "parent",column = "schema=schema, id=parent",one = @One(select = "findById"))
    })
    Department findById(@Param("schema") String schema, @Param("id") int id);

    @Select("select * from ${schema}.department where parent is null order by no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class)
    })
    List<Department> listParent(@Param("schema") String schema);

    @Select("select '${schema}' as schema , * from ${schema}.department where parent=#{id} order by no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
            @Result(property = "id",column = "id"),
            @Result(property = "children",column = "schema=schema, id=id",many = @Many(select = "listAllByParent"))
    })
    List<Department> listAllByParent(@Param("schema") String schema, @Param("id") int id);

    @Select("select '${schema}' as schema , * from ${schema}.department order by parent,no,id")
    @Results({
            @Result(property = "extend",column = "extend",typeHandler = JsonbHandler.class),
            @Result(property = "id",column = "id"),
            @Result(property = "children",column = "schema=schema, id=id",many = @Many(select = "listAllByParent")),
            @Result(property = "parent",column = "schema=schema, id=parent",one = @One(select = "findById"))
    })
    List<Department> listAll(@Param("schema") String schema);
}
