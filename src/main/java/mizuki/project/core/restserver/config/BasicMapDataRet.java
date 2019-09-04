package mizuki.project.core.restserver.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本的返回map+ map data
 */
public class BasicMapDataRet extends BasicRet{

    private Map<String,Object> data = new HashMap<>();

    public BasicMapDataRet(){}

    public BasicMapDataRet(int result){
        this.result=result;
    }

    public BasicMapDataRet(int result, String message){
        this(result);
        this.message=message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public BasicMapDataRet setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
