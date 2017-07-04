package mizuki.project.core.restserver.config.mybatis.provider;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Id {

    String value() default "";

    /** 是否需要insert */
    boolean insert() default true;
}