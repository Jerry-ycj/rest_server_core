package mizuki.project.core.restserver.modules.session;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by ycj on 2017/3/22.
 *
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)
public class SpringSessionConfig {

//    @Autowired
//    private RedisOperationsSessionRepository sessionRepository;
//
//    @PostConstruct
//    private void init(){
//        // 设置session expire时间（秒）
//        sessionRepository.setDefaultMaxInactiveInterval(60*60);
//    }

}
