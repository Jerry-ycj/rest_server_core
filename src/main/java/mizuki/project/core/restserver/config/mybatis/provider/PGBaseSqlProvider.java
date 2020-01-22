package mizuki.project.core.restserver.config.mybatis.provider;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * OneToOne必须 - joinColumn选填 其中name和referencedColumnName必须
 * id & GeneratedValue - 代表外部生成id
 *
 */
public class PGBaseSqlProvider {

	public static final String SCHEMA = "public";

    public static final String METHOD_INSERT="insert";
    public static final String METHOD_UPDATEALL="updateAll";
    public static final String METHOD_DELETE="delete";
    public static final String METHOD_DELETE_OFF="deleteWithOff";
    public static final String METHOD_FIND_ONE = "findOne";

//	public static final String METHOD_INSERT_BY_SCHEMA="insertBySchema";
//	public static final String METHOD_UPDATE_BY_SCHEMA="updateAllBySchema";
//	public static final String METHOD_DELETE_BY_SCHEMA="deleteBySchema";
//	public static final String METHOD_DELETE_OFF_BY_SCHEMA="deleteWithOffBySchema";
//	public static final String METHOD_FIND_ONE_BY_SCHEMA = "findOneBySchema";

	public String insert(Object bean) throws IllegalAccessException {
		return insert(null, bean);
	}
	public String insert(String schema, Object bean) throws IllegalAccessException {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		Map<String,String> kvs = new HashMap<>();
		for (Field field : fields) {
			if(annotationExist(field,Id.class) && annotationExist(field, GeneratedValue.class)) continue;
			genKvs(bean, kvs, field);
		}
		return new SQL(){{
			INSERT_INTO(schema==null?"public."+tableName:schema+"."+tableName);
			for(String k:kvs.keySet()){
				VALUES(k,kvs.get(k));
			}
		}}.toString();
	}

	/** update all by id */
	public String updateAll(String schema, Object bean) throws IllegalAccessException {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		Map<String,String> kvs = new HashMap<>();
		Map<String,String> idKvs = new HashMap<>();
		for(Field field:fields){
			if(annotationExist(field,Id.class)){
				genKvs(bean,idKvs,field);
				continue;
			}
			genKvs(bean, kvs, field);
		}
		return new SQL(){{
			UPDATE(schema==null?"public."+tableName:schema+"."+tableName);
			for(String k:kvs.keySet()){
				SET(k+"="+kvs.get(k));
			}
			for (String k:idKvs.keySet()){
				WHERE(k+"="+idKvs.get(k));
			}

		}}.toString();
	}
	public String updateAll(Object bean) throws IllegalAccessException {
		return updateAll(null, bean);
	}

	public String delete(String schema, Object bean) throws IllegalAccessException {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		Map<String,String> idKvs = new HashMap<>();
		for(Field field:fields){
			if(annotationExist(field,Id.class)){
				genKvs(bean,idKvs,field);
			}
		}
		return new SQL(){{
			DELETE_FROM(schema==null?"public."+tableName:schema+"."+tableName);
			for (String k:idKvs.keySet()){
				WHERE(k+"="+idKvs.get(k));
			}
		}}.toString();
	}
	public String delete(Object bean) throws IllegalAccessException {
		return delete(null, bean);
	}

	public String deleteWithOff(String schema, Object bean) throws IllegalAccessException {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		Map<String,String> idKvs = new HashMap<>();
		for(Field field:fields){
			if(annotationExist(field,Id.class)){
				genKvs(bean,idKvs,field);
			}
		}
		return new SQL(){{
			UPDATE(schema==null?"public."+tableName:schema+"."+tableName);
			SET("off=true");
			for (String k:idKvs.keySet()){
				WHERE(k+"="+idKvs.get(k));
			}
		}}.toString();
	}
	public String deleteWithOff(Object bean) throws IllegalAccessException {
		return deleteWithOff(null,bean);
	}

	public String findOne(String schema, Object bean, String key){
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
//		Field[] fields = getFields(beanClass);
		return new SQL(){{
			SELECT("*");
			FROM(schema==null?"public."+tableName:schema+"."+tableName);
			WHERE(String.format("%s=#{param1.%s}",key,key));
		}}.toString();
	}
	public String findOne(Object bean, String key){
		return findOne(null, bean, key);
	}

	/***
	 * 如果没有@table, 那么按类名同名,首字母小写
	 */
	private static String getTableName(Class<?> beanClass) {
		String tableName;
		Table table = beanClass.getAnnotation(Table.class);
		if (table != null) {
			tableName = table.name();
		} else {
			tableName = beanClass.getSimpleName();
			tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
		}
		return tableName;
	}

	private static Field[] getFields(Class<?> beanClass) {
		Field[] beanFields = beanClass.getDeclaredFields();
		Class<?> beanSuperClass = beanClass.getSuperclass();
		Field[] beanSuperFields = beanSuperClass.getDeclaredFields();
		return ArrayUtils.addAll(beanFields, beanSuperFields);
	}

	private boolean annotationExist(Field field, Class clz) {
		return field.getAnnotation(clz)!=null;
	}

	private void genKvs(Object bean, Map<String, String> kvs, Field field) throws IllegalAccessException {
		if(annotationExist(field,Transient.class)) return;
		field.setAccessible(true);
		Object object = field.get(bean);
		if(object==null) return;
		String key = field.getName();
		String val = "#{"+field.getName()+"}";

		// 通常column
		Column column = field.getAnnotation(Column.class);
		if(column!=null && !"".equals(column.name())) key = column.name();
		// 对象 column
		if(annotationExist(field,OneToOne.class)){
			JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
			if(joinColumn!=null){
				key = joinColumn.name();
				val = "#{"+field.getName()+"."+joinColumn.referencedColumnName()+"}";
			}else{
				val = "#{"+field.getName()+".id}";
			}
		}

		// jsonb
		if(object instanceof Map){
			val = "#{"+field.getName()+",typeHandler=mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonbHandler}";
		}
		// array todo
		if(object instanceof Collection){
			if(field.getGenericType().getTypeName().contains("java.util.Map")){
				// list<Map> 看待为 jsonb
				val = "#{"+field.getName()+",typeHandler=mizuki.project.core.restserver.config.mybatis.typeHandler.jsonb.JsonbHandler}";
			} else if(field.getGenericType().getTypeName().contains("<java.lang.String>")){
				val = "#{"+field.getName()+",typeHandler=mizuki.project.core.restserver.config.mybatis.typeHandler.array.StringArrayHandler}";
			}else if(field.getGenericType().getTypeName().contains("<java.lang.Integer>")){
				val = "#{"+field.getName()+",typeHandler=mizuki.project.core.restserver.config.mybatis.typeHandler.array.IntArrayHandler}";
			}
		}
		kvs.put(key,val);
	}

	public static void main(String[] args) throws Exception {
		HashMap<String,Object> map = new HashMap<>();
		map.put("key1","'val");
		map.put("key2",12);
		List<String> list = new ArrayList<>();
		list.add("12");list.add("23");
		System.out.println(System.currentTimeMillis());
		System.out.println(new PGBaseSqlProvider().insert(new Test().setName("'sss'")
				.setName("abc").setRole(new Test().setId(111)).setMap(map).setList(list)));
		System.out.println(new PGBaseSqlProvider().updateAll(new Test()
				.setName("abc").setRole(new Test().setId(111)).setMap(map).setList(list)));
		System.out.println(new PGBaseSqlProvider().updateAll(new Test()
				.setName("abc").setRole(new Test().setId(111)).setList(list)));
		System.out.println(new PGBaseSqlProvider().findOne(new Test()
				.setName("abc").setRole(new Test().setId(111)).setMap(map).setList(list),"id"));
		System.out.println(System.currentTimeMillis());

	}

}
