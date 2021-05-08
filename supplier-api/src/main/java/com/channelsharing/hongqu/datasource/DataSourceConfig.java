package com.channelsharing.hongqu.datasource;

import com.channelsharing.common.datasource.DruidConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = {DataSourceConfig.PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
public class
DataSourceConfig {

    // 精确到目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.channelsharing.hongqu.supplier.api.dao, com.channelsharing.pub.dao";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initial-size}")
    private String initialSize;

    @Value("${spring.datasource.min-idle}")
    private String minIdle;

    @Value("${spring.datasource.max-idle}")
    private String maxIdle;

    @Value("${spring.datasource.max-active}")
    private String maxActive;

    @Value("${spring.datasource.max-wait}")
    private String maxWait;

    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    private String timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    private String minEvictableIdleTimeMillis;

    @Value("${spring.datasource.test-while-idle}")
    private String testWhileIdle;

    @Value("${spring.datasource.test-on-borrow}")
    private String testOnBorrow;

    @Value("${spring.datasource.test-on-return}")
    private String testOnReturn;

    @Bean(name = "dataSource")
    public DataSource dataSource() {

        DruidConfig druidConfig = DruidConfig.builder()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .initialSize(initialSize)
                .minIdle(minIdle)
                .maxIdle(maxIdle)
                .maxActive(maxActive)
                .maxWait(maxWait)
                .timeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis)
                .minEvictableIdleTimeMillis(minEvictableIdleTimeMillis)
                .testWhileIdle(testWhileIdle)
                .testOnBorrow(testOnBorrow)
                .testOnReturn(testOnReturn)
                .build();

        return druidConfig.dataSource("dataSource");
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
