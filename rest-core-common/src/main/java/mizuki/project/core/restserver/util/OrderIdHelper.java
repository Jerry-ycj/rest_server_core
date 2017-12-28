package mizuki.project.core.restserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderIdHelper {
    private static final Map<String,Object> locks = new HashMap<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 时间+5位数; type 不同业务
     */
    public static String genId(String type){
        locks.computeIfAbsent(type, k -> new HashMap<String,Object>());
        Map<String,Object> lock = (Map<String, Object>) locks.get(type);
        synchronized (lock){
            int count = (int)lock.getOrDefault("count",1);
            lock.put("count",count+1);
            return simpleDateFormat.format(new Date()) +String.format("%05d", count&0xffff);
        }
    }

}
