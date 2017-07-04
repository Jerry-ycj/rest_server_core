package mizuki.project.core.restserver.config.mybatis.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ycj on 2017/4/13.
 *  {1,2,3} - list
 *
 */
public class IntArrayHandler implements TypeHandler<List<Integer>>{

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        //todo list->sql_param
    }

    @Override
    public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getString(columnName));
    }

    @Override
    public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getString(columnIndex));
    }

    private List<Integer> transfer(String s){
        List<Integer> list = new ArrayList<>();
        if(s==null || s.length()==2){
            return list;
        }
        String tmp = s.substring(1,s.length()-1);
        for(String e:tmp.split(",")){
            list.add(Integer.parseInt(e));
        }
        return list;
    }
}
