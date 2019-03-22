package mizuki.project.core.restserver.util;

import java.util.HashMap;
import java.util.Map;

public class MapHelper {

    /**
     * 判断map中val是否为null，并给出default
     */
    public static Object getOrDefault(Map map,String key, Object d){
        if(map==null || map.get(key)==null) return d;
        else return map.get(key);
    }

    /***
     * 合并，map2合并入map1
     */
    public static void merge(Map<String,Object> map1, Map<String,Object> map2){
        if(map2==null) return;
        for(String key:map2.keySet()){
            Object obj = map2.get(key);
            if(obj instanceof Map && map1.get(key)!=null && map1.get(key) instanceof Map){
                merge((Map<String, Object>) map1.get(key),(Map<String, Object>) obj);
            }else if(obj instanceof Map){
                // 防止操作原map
                Map<String,Object> temp = new HashMap<>();
                merge(temp,(Map<String, Object>) obj);
                map1.put(key,temp);
            }else{
                map1.put(key,obj);
            }
        }
    }

}
