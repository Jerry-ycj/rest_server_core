package mizuki.project.core.restserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderIdHelper {
    private static final Map<String,Object> locks = new HashMap<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");

    /**
     * 时间+ max-5位数(一秒16bit-65536个); type 不同业务
     */
    public static String genId(String type){
        locks.computeIfAbsent(type, k -> new HashMap<String,Object>());
        Map<String,Object> lock = (Map<String, Object>) locks.get(type);
        synchronized (lock){
            int count = (int)lock.getOrDefault("count",1);
            lock.put("count",count+1);
            return simpleDateFormat.format(new Date()) +String.valueOf(count&0xffff);
        }
    }

    /**
     * pre+时间+1-3位数 (一秒8bit-128个); type 不同业务
     */
    public static String genId4(String pre,String type){
        locks.computeIfAbsent(type, k -> new HashMap<String,Object>());
        Map<String,Object> lock = (Map<String, Object>) locks.get(type);
        synchronized (lock){
            int count = (int)lock.getOrDefault("count",1);
            lock.put("count",count+1);
            return pre+simpleDateFormat.format(new Date()) +String.valueOf(count&0xff);
        }
    }

}
