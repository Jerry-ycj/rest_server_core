package mizuki.project.core.restserver.util;

import mizuki.project.core.restserver.config.exception.RestMainException;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by ycj on 2016/12/4.
 *
 *
 */
public class OtherUtil {

    public static final String RANDOM2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String genNickname(String phone){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<4;i++){
            sb.append(RANDOM2.charAt(random.nextInt(RANDOM2.length())));
        }
        sb.append(phone.substring(phone.length()-4));
        return sb.toString();
    }

    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    public static boolean isNilString(String str){
        return str==null || "".equals(str.trim());
    }

    public static Map<String,Object> getExtendJson(String extendJson) throws RestMainException {
        Map<String,Object> extend=null;
        if(extendJson!=null) {
            extend = JsonUtil.toMap(extendJson);
            if(extend==null) throw new RestMainException("extend参数解析错误");
        }
        return extend;
    }
}
