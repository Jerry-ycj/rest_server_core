package mizuki.project.core.restserver.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by ycj on 2017/4/12.
 */
public class StringUtil {

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
        return Arrays.stream(array).map(Object::toString).collect(Collectors.joining(separator));
//        StringBuilder stringBuilder = new StringBuilder();
//        Arrays.stream(array).forEach(a-> stringBuilder.append(a).append(separator));
//        return stringBuilder.substring(0,stringBuilder.length()-separator.length());
    }

    /***
     * eg: list to e,e,e
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
        if(el_decorate==null){
            return join(array, separator);
        }
        return Arrays.stream(array).map(str->el_decorate+str+el_decorate).collect(Collectors.joining(separator));
//        Arrays.stream(array).forEach(a->{
//            if(el_decorate!=null){
//                stringBuilder.append(el_decorate).append(a)
//                        .append(el_decorate).append(separator);
//            }else{
//                stringBuilder.append(a).append(separator);
//            }
//        });
//        return stringBuilder.substring(0,stringBuilder.length()-separator.length());
    }

    public static void main(String[] args) {
        System.out.println(join(new String[]{"a","b","c"}, ","));
    }

}
