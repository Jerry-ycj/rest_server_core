package mizuki.project.core.restserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by ycj on 16/5/19.
 * 用于web server的相关私有配置
 */
@ConfigurationProperties("project")
public class WebConfBean {

    private String projectDomain;
    private String projectStaticPre;
    private String projectPath;
    private String projectDomainMain;
    private String iconfontPath;
    private String staticResVersion;
    @Value("${server.context-path}")
    private String contextPath;
    // nginx https限制
    private boolean forceNginxHttps;

    private SpringConfBean springConfBean;

    @Autowired
    public WebConfBean setSpringConfBean(SpringConfBean springConfBean) {
        this.springConfBean = springConfBean;
        return this;
    }

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

    public SpringConfBean getSpringConfBean() {
        return springConfBean;
    }

    public String getContextPath() {
        return contextPath;
    }

    public WebConfBean setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public boolean isForceNginxHttps() {
        return forceNginxHttps;
    }

    public WebConfBean setForceNginxHttps(boolean forceNginxHttps) {
        this.forceNginxHttps = forceNginxHttps;
        return this;
    }
}
