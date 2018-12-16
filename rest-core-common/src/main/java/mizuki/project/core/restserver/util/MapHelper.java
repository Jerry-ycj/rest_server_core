package mizuki.project.core.restserver.util;

import java.util.Map;

public class MapHelper {

    /**
     * 判断map中val是否为null，并给出default
     */
    public static Object getOrDefault(Map map,String key, Object d){
        if(map==null || map.get(key)==null) return d;
        else return map.get(key);
    }

}
