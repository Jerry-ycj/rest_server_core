package mizuki.project.core.restserver.config.mybatis.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ycj on 2017/4/13.
 *  {'',''} - list
 *
 */
public class StringArrayHandler implements TypeHandler<List<String>>{

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        //todo list->sql_param
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getString(columnName));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getString(columnIndex));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getString(columnIndex));
    }

    private List<String> transfer(String s){
        List<String> list = new ArrayList<>();
        if(s==null || s.length()==2){
            return list;
        }
        String tmp = s.substring(1,s.length()-1);
        list.addAll(Arrays.asList(tmp.split(",")));
        return list;
    }
}
