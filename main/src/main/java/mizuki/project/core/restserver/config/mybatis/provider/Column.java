package mizuki.project.core.restserver.config.mybatis.provider;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Column {

    String value() default "";

    boolean required() default true;
}