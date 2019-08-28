package mizuki.project.core.restserver.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ycj on 2016/11/3.
 *
 */
@Configuration
@ConfigurationProperties("mybatis.pgsql1")
public class DataSourcePgsql1Conf {

    private String url;
    private String username;
    private String password;

    @Bean(name = "dataSource_pgsql1")
    @Primary
    public DataSource dataSource(){
        return ConfigHelper.genDefaultDruidDataSource("org.postgresql.Driver",
                url,username,password);
    }

    /**
     * spring transaction mng
     */
    @Bean(name = "transactionManager_pgsql1")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource_pgsql1") DataSource rdsDataSource) {
        return new DataSourceTransactionManager(rdsDataSource);
    }

    /**
     * mybatis session factory
     */
    @Bean(name = "sqlSessionFactory_pgsql1")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource_pgsql1")DataSource rdsDataSource) throws Exception {
        return ConfigHelper.genDefaultSqlSessionFactory(rdsDataSource);
    }

    public String getUrl() {
        return url;
    }

    public DataSourcePgsql1Conf setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DataSourcePgsql1Conf setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DataSourcePgsql1Conf setPassword(String password) {
        this.password = password;
        return this;
    }
}
