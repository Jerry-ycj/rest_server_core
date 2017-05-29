package mizuki.project.core.restserver.util;

import java.util.List;

/**
 * Created by ycj on 2017/4/12.
 *
 */
public class StringUtil {

    /***
     * list to e,e,e
     */
    public static String join(List list,String separator){
        if(list==null){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<list.size();i++){
            stringBuilder.append(list.get(i));
            if(i<list.size()-1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }
}
