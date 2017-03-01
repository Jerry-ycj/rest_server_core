package mizuki.project.core.restserver.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by ycj on 16/5/19.
 * 用于web server的相关私有配置
 */
@Component
@ConfigurationProperties(locations = "classpath:server.yml",prefix = "project")
public class WebConfBean {

    private String projectDomain;
    private String projectStaticPre;
    private String projectPath;
    private String projectDomainMain;
    private String iconfontPath;
    private String staticResVersion;

    public WebConfBean() {}

    @PostConstruct
    public void init(){
        if(projectPath==null){
            return;
        }
        File file = new File(projectPath);
        if(!file.exists()){
            boolean ok = file.mkdirs();
            // TODO
        }
    }

    public WebConfBean setProjectDomain(String projectDomain) {
        this.projectDomain = projectDomain;
        return this;
    }

    public WebConfBean setProjectStaticPre(String projectStaticPre) {
        this.projectStaticPre = projectStaticPre;
        return this;
    }

    public WebConfBean setProjectPath(String projectPath) {
        this.projectPath = projectPath;
        return this;
    }

    public WebConfBean setProjectDomainMain(String projectDomainMain) {
        this.projectDomainMain = projectDomainMain;
        return this;
    }

    public String getProjectDomain() {
        return projectDomain;
    }

    public String getProjectStaticPre() {
        return projectStaticPre;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getProjectDomainMain() {
        return projectDomainMain;
    }

    public String getIconfontPath() {
        return iconfontPath;
    }

    public WebConfBean setIconfontPath(String iconfontPath) {
        this.iconfontPath = iconfontPath;
        return this;
    }

    public String getStaticResVersion() {
        return staticResVersion;
    }

    public WebConfBean setStaticResVersion(String staticResVersion) {
        this.staticResVersion = staticResVersion;
        return this;
    }
}
