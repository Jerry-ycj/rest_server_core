package mizuki.project.core.restserver.config.mybatis.provider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class BasicSqlProvider {

	public String insert(Object bean) throws Exception {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder insertSql = new StringBuilder();
		List<String> insertParas = new ArrayList<>();
		List<String> insertParaNames = new ArrayList<>();
		insertSql.append("INSERT INTO ").append(tableName).append("(");
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
            Id columnId = field.getAnnotation(Id.class);
			String columnName = "";
            /* id */
            if(columnId != null){
                if(!columnId.insert()) continue;
                columnName = columnId.value();
            }
            /** column */
			else if (column != null) {
				if (!column.required()) continue;
				columnName = column.value();
			}
			if (StringUtils.isEmpty(columnName)) {
				columnName = field.getName();
			}
			field.setAccessible(true);
			/** 存在才insert **/
			Object object = field.get(bean);
			if (object != null) {
				insertParaNames.add(columnName);
				insertParas.add("#{" + field.getName() + "}");
			}
		}

		for (int i = 0; i < insertParaNames.size(); i++) {
			insertSql.append(insertParaNames.get(i));
			if (i != insertParaNames.size() - 1)
				insertSql.append(",");
		}
		insertSql.append(")").append(" VALUES(");
		for (int i = 0; i < insertParas.size(); i++) {
			insertSql.append(insertParas.get(i));
			if (i != insertParas.size() - 1)
				insertSql.append(",");
		}
		insertSql.append(")");
		return insertSql.toString();
	}

	/**
	 * 全体update, by id
	 */
	public String update(Object bean) throws Exception {
		Class<?> beanClass = bean.getClass();
		String tableName = getTableName(beanClass);
		Field[] fields = getFields(beanClass);
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update ").append(tableName).append(" set ");
		// id
		String id = "id";
        String idFieldName = "id";
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Column column = field.getAnnotation(Column.class);
            Id columnId = field.getAnnotation(Id.class);
			String columnName = "";
			if (column != null) {
				if (!column.required()) continue;
				columnName = column.value();
			}
			if (StringUtils.isEmpty(columnName)) {
				columnName = field.getName();
			}
            /** 修改id */
			if(columnId != null){
                id = columnName;
                idFieldName = field.getName();
                if(!StringUtils.isEmpty(columnId.value())){
                    id=columnId.value();
                }
                continue;
			}
			field.setAccessible(true);
			Object beanValue = field.get(bean);
			if (beanValue != null && !id.equals(columnName)) {
				updateSql.append(columnName).append("=#{").append(field.getName()).append("}");
				if (i != fields.length - 1) {
					updateSql.append(",");
				}
			}
		}
		if(updateSql.charAt(updateSql.length()-1)==',') {
			updateSql.deleteCharAt(updateSql.length() - 1);
		}
		updateSql.append(" where ").append(id).append("=#{").append(idFieldName).append("}");
		return updateSql.toString();
	}

	/***
	 * 如果没有@table, 那么按类名同名,首字母小写
	 */
	protected String getTableName(Class<?> beanClass) {
		String tableName;
		Table table = beanClass.getAnnotation(Table.class);
		if (table != null) {
			tableName = table.value();
		} else {
			tableName = beanClass.getSimpleName();
			tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
		}
		return tableName;
	}

	protected Field[] getFields(Class<?> beanClass) {
		Field[] beanFields = beanClass.getDeclaredFields();
		Class<?> beanSuperClass = beanClass.getSuperclass();
		Field[] beanSuperFields = beanSuperClass.getDeclaredFields();
		return ArrayUtils.addAll(beanFields, beanSuperFields);
	}

//    public static void main(String[] args) throws Exception {
//        System.out.println(new BasicSqlProvider().update(new TestBean().setHaha("223")));
//        System.out.println(new BasicSqlProvider().insert(new TestBean().setHaha("223")));
//    }
}
