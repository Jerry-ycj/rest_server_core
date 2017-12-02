package mizuki.project.core.restserver.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by ycj on 2017/4/12.
 *
 */
public class StringUtil {

    /***
     * list to e,e,e
     */
    public static String join(Collection list,String separator){
        if(list==null){
            return "";
        }
        return join(list.toArray(),separator);
    }

    public static String join(Object[] array, String separator){
        if(array==null || array.length==0){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(array).forEach(a-> stringBuilder.append(a).append(separator));
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    /***
     * list to e,e,e
     * el_decorate 修饰： "e"
     */
    public static String join(Collection list, String separator, String el_decorate){
        if(list==null){
            return "";
        }
        return join(list.toArray(),separator,el_decorate);
    }

    public static String join(Object[] array,String separator, String el_decorate){
        if(array==null || array.length==0){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(array).forEach(a->{
            if(el_decorate!=null){
                stringBuilder.append(el_decorate).append(a)
                        .append(el_decorate).append(separator);
            }else{
                stringBuilder.append(a).append(separator);
            }
        });
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
