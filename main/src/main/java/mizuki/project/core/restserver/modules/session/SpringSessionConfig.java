package mizuki.project.core.restserver.modules.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;

/**
 * Created by ycj on 2017/3/22.
 *
 */
@EnableRedisHttpSession
public class SpringSessionConfig {

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @PostConstruct
    private void init(){
        // 设置session expire时间（秒）
        sessionRepository.setDefaultMaxInactiveInterval(60*60);
    }


}
