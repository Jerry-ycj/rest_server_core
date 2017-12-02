package mizuki.project.core.restserver.config;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${var.server}")
    private String ip;
    @Value("${server.port}")
    private int port;

    public String getProfiles() {
        return profiles;
    }

    public SpringConfBean setProfiles(String profiles) {
        this.profiles = profiles;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SpringConfBean setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SpringConfBean setPort(int port) {
        this.port = port;
        return this;
    }
}
