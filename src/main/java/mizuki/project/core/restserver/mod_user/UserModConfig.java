package mizuki.project.core.restserver.mod_user;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        UserRestAction.class,
        AdminUserRestAction.class,
        AdminRoleRestAction.class
})
public class UserModConfig {

}
