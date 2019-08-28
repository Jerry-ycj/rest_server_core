package mizuki.project.core.restserver.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String toJson(Object obj) {
        try {
            return obj==null?null: JSONObject.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("json parse error:",e);
        }
        return null;
    }

    public static Map toMap(String str){
        try {
            return str==null||"".equals(str.trim())?null:JSONObject.parseObject(str,Map.class);
        } catch (Exception e) {
            logger.error("json parse error:",e);
        }
        return null;
    }

    public static <T> T toObject(String str, Class<T> clas){
        try {
            return str==null||"".equals(str.trim())?null:JSONObject.parseObject(str,clas);
        } catch (Exception e) {
            logger.error("json parse error:",e);
        }
        return null;
    }
}
