package mizuki.project.core.restserver.config.mybatis.helper;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SqlHelper {

    /**
     * 批量jsonb select时的 生成一个string
     */
    boolean jsonb_select_field() default true;

}