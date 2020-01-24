package mizuki.project.core.restserver.mod_user;

import mizuki.project.core.restserver.modules.session.SpringSessionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        UserRestAction.class,
        AdminUserRestAction.class,
        AdminRoleRestAction.class,
        UserCenter.class,
        SpringSessionService.class
})
public class UserModConfig {

}
