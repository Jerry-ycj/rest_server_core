package mizuki.project.core.restserver.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import javax.sql.DataSource;

/**
 * Created by ycj on 2017/2/22.
 * 配置 辅助类
 */
public class ConfigHelper {

    public static DataSource genDefaultDruidDataSource(String driver,String url,String username,String pwd){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(pwd);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("select 1");
//        dataSource.setDefaultAutoCommit(false); // 默认事务管理器提交的,没用
        return dataSource;
    }

    public static SqlSessionFactory genDefaultSqlSessionFactory(DataSource rdsDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(rdsDataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setLazyLoadingEnabled(true);
//        configuration.setAggressiveLazyLoading(false);
        configuration.setCacheEnabled(true);
        sessionFactory.setConfiguration(configuration);
//        Resource[] resources = ctx.getResources("classpath:mybatis_xml/*");
//        sessionFactory.setMapperLocations(resources);
        return sessionFactory.getObject();
    }

}
