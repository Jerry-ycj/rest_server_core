package mizuki.project.core.restserver.config.mybatis.typeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ycj on 2017/4/13.
 *  {} - Map
 *
 */
public class JsonHandler implements TypeHandler<Map>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
        //todo list->sql_param
    }

    @Override
    public Map getResult(ResultSet rs, String columnName) throws SQLException {
        return transfer(rs.getString(columnName));
    }

    @Override
    public Map getResult(ResultSet rs, int columnIndex) throws SQLException {
        return transfer(rs.getString(columnIndex));
    }

    @Override
    public Map getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return transfer(cs.getString(columnIndex));
    }

    private Map transfer(String s){
        // "{\"test\": 2,\"key\": 12}"
        Map<String,Object> map = new HashMap<>();
        if(s==null || s.length()==2){
            return map;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            map = objectMapper.readValue(s,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("err：",e);
        }
        return map;
    }
}
