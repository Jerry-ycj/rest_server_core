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
 *  {1,2,3} - list
 *
 */
public class IntArrayHandler implements TypeHandler<Collection<Integer>>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setParameter(PreparedStatement ps, int i, Collection<Integer> parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf("integer", parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public Collection<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getArray(columnName));
    }

    @Override
    public Collection<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getArray(columnIndex));
    }

    @Override
    public Collection<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getArray(columnIndex));
    }

    private List<Integer> transfer(Array s){
        if(s==null){
            return new ArrayList<>();
        }
        try {
            return List.of((Integer[]) s.getArray());
        } catch (SQLException e) {
            logger.error("error: ",e);
            return new ArrayList<>();
        }
//        String tmp = s.substring(1,s.length()-1);
//        for(String e:tmp.split(",")){
//            if(e.contains("\"")) e=e.replaceAll("\"","");
//            list.add(Integer.parseInt(e));
//        }
    }


}
