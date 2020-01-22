package mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb;

import mizuki.project.core.restserver.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class JsonArrayHandler extends BaseTypeHandler<Object> {

    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("jsonb");
        jsonObject.setValue(JsonUtil.toJson(parameter));
        ps.setObject(i,jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getString(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getString(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getString(columnIndex));
    }

    private List<Map> transfer(String s){
        // "[{\"test\": 2,\"key\": 12},{}]"
        if(s==null || s.length()==2){
            return new ArrayList<>();
        }
        List list = JsonUtil.toObject(s,List.class);
        if(list==null){
            return new ArrayList<>();
        }else{
            return list;
        }

    }
}
