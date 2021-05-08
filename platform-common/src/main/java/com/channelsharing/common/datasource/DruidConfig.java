package com.channelsharing.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by liuhangjun on 2018/2/2.
 */
@Builder
@Data
public class DruidConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String initialSize;
    private String minIdle;
    private String maxIdle;
    private String maxWait;
    private String maxActive;
    private String minEvictableIdleTimeMillis;
    private String timeBetweenEvictionRunsMillis;
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;


    /**
     *
     * @param dbName
     * @return
     */
    public DataSource dataSource(String dbName) {

        System.out.println();
        System.out.println("=================== " + dbName + " datasource 注入druid！！！=================");
        System.out.println();

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(Integer.valueOf(initialSize));
        dataSource.setMinIdle(Integer.valueOf(minIdle));
        dataSource.setMaxWait(Long.valueOf(maxWait));
        dataSource.setMaxActive(Integer.valueOf(maxActive));
        dataSource.setMinEvictableIdleTimeMillis(
                Long.valueOf(minEvictableIdleTimeMillis));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(timeBetweenEvictionRunsMillis));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
        dataSource.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
        dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
        try {
            dataSource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
