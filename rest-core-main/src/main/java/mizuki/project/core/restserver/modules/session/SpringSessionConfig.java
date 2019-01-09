package mizuki.project.core.restserver.modules.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Created by ycj on 2017/3/22.
 *
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)
@Import({
        SpringSessionService.class
})
public class SpringSessionConfig {

//    @Autowired
//    private RedisOperationsSessionRepository sessionRepository;
//
//    @PostConstruct
//    private void init(){
//        // 设置session expire时间（秒）
//        sessionRepository.setDefaultMaxInactiveInterval(60*60);
//    }
    @Bean
    public CookieSerializer httpSessionIdResolver(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 使跨域能携带cookies（post）
        cookieSerializer.setSameSite(null);
        return cookieSerializer;
    }

}
