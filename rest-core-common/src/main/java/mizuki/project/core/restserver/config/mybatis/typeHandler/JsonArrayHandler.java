package mizuki.project.core.restserver.config.mybatis.typeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import mizuki.project.core.restserver.util.JsonUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycj on 2017/4/13.
 *  [{},{}] - list<Map>
 *
 */
@Deprecated
public class JsonArrayHandler implements TypeHandler<List<Map>>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Map> parameter, JdbcType jdbcType) throws SQLException {
        //todo list->sql_param
    }

    @Override
    public List<Map> getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getString(columnName));
    }

    @Override
    public List<Map> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getString(columnIndex));
    }

    @Override
    public List<Map> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getString(columnIndex));
    }

    private List<Map> transfer(String s){
        // "[{\"test\": 2,\"key\": 12},{}]"
        Map<String,Object> map = new HashMap<>();
        if(s==null || s.length()==2){
            return new ArrayList<>();
        }
        List list = (List) JsonUtil.toObject(s,List.class);
        if(list==null){
            return new ArrayList<>();
        }else{
            return list;
        }

    }
}
