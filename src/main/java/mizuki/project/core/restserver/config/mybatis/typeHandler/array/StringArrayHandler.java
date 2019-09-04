package mizuki.project.core.restserver.config.mybatis.typeHandler.array;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ycj on 2017/4/13.
 *  {'',''} - list
 *
 */
public class StringArrayHandler implements TypeHandler<Collection<String>>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setParameter(PreparedStatement ps, int i, Collection<String> parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf("varchar", parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public Collection<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getArray(columnName));
    }

    @Override
    public Collection<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getArray(columnIndex));
    }

    @Override
    public Collection<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getArray(columnIndex));
    }

    private List<String> transfer(Array s){
        if(s==null){
            return new ArrayList<>();
        }
        try {
            return List.of((String[]) s.getArray());
        } catch (SQLException e) {
            logger.error("error: ",e);
            return new ArrayList<>();
        }
//        s = s.replace("{\"","{")
//                .replace("\",\"",",")
//                .replace("\"}","}");
//        String tmp = s.substring(1,s.length()-1);
//        list.addAll(Arrays.asList(tmp.split(",")));
    }
}
