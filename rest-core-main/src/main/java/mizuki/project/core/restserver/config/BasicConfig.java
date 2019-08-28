package mizuki.project.core.restserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringConfBean.class,
        WebConfBean.class,
        ExceptionProcess.class
})
public class BasicConfig {

}
