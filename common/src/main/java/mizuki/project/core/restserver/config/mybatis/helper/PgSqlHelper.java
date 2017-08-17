package mizuki.project.core.restserver.config.mybatis.helper;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/**
 * 用于pgsql中sql语句里的 数据整合处理
 */
public class PgSqlHelper {

    public static String genJsonbSelectField(Class clas,String jsonbField){
        Field[] fields = getFields(clas);
        StringBuilder res = new StringBuilder();
        for(Field field:fields){
            SqlHelper sqlHelper = field.getAnnotation(SqlHelper.class);
            if(sqlHelper!=null && sqlHelper.jsonb_select_field()) {
                res.append(jsonbField).append("->'")
                        .append(field.getName())
                        .append("' ").append(field.getName())
                        .append(",");
            }
        }
        res.deleteCharAt(res.length()-1);
        return res.toString();
    }

    private static Field[] getFields(Class<?> beanClass) {
        Field[] beanFields = beanClass.getDeclaredFields();
        Class<?> beanSuperClass = beanClass.getSuperclass();
        Field[] beanSuperFields = beanSuperClass.getDeclaredFields();
        return ArrayUtils.addAll(beanFields, beanSuperFields);
    }

}
