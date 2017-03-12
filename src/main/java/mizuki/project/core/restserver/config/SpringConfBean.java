package mizuki.project.core.restserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ycj on 2017/2/11.
 * 对应 application.yml中spring的配置
 */
@ConfigurationProperties("spring")
public class SpringConfBean {

    public static final String PROFILE_PRO = "pro";
    public static final String PROFILE_DEV = "dev";

    // 不用那个active
    private String profiles;

    public String getProfiles() {
        return profiles;
    }

    public SpringConfBean setProfiles(String profiles) {
        this.profiles = profiles;
        return this;
    }

}
