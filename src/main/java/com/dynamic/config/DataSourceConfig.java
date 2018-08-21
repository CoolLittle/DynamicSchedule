package com.dynamic.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@PropertySources({@PropertySource(
        value = {"classpath:jdbc.properties"},
        encoding = "UTF-8"
)})
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.driver}")
    private String dirver;

    @Value("${druid.initialSize}")
    private Integer initialSize;

    @Value("${druid.minIdle}")
    private Integer minIdle;

    @Value("${druid.removeAbandoned}")
    private Boolean removeAbandoned;

    @Value("${druid.removeAbandonedTimeout}")
    private Integer removeAbandonedTimeout;

    @Value("${druid.maxActive}")
    private Integer maxActive;

    @Value("${druid.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${druid.maxWait}")
    private Long maxWait;

    @Value("${druid.poolPreparedStatements}")
    private Boolean poolPreparedStatements;

    @Value("${druid.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("${druid.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${druid.validationQuery}")
    private String validationQuery;

    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${druid.filters}")
    private String filters;

    @Value("${druid.connectionProperties}")
    private Properties connectionProperties;

    @Resource
    private Slf4jLogFilter logFilter;

    @Autowired
    private StatFilter statFilter;

    /**
     * 数据库连接池，阿里巴巴的德鲁伊
     * @return
     */
    @Bean
    public DataSource dataSource() {

        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setUrl(this.url);
        druidDataSource.setUsername(this.username);
        druidDataSource.setPassword(this.password);
        druidDataSource.setDriverClassName(this.dirver);
        druidDataSource.setInitialSize(this.initialSize);
        druidDataSource.setMinIdle(this.minIdle);
        druidDataSource.setRemoveAbandoned(this.removeAbandoned);
        druidDataSource.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
        druidDataSource.setMaxActive(this.maxActive);
        druidDataSource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
        druidDataSource.setMaxWait(this.maxWait);
        druidDataSource.setPoolPreparedStatements(this.poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setTestWhileIdle(this.testWhileIdle);
        druidDataSource.setValidationQuery(this.validationQuery);
        druidDataSource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        try {
            druidDataSource.setFilters(this.filters);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("druid configuration initialization filter", e);
        }
        //druidDataSource.set
        druidDataSource.setConnectProperties(this.connectionProperties);
        List list= new ArrayList<Filter>(){{add(logFilter);add(statFilter);}};
        druidDataSource.setProxyFilters(list);
        druidDataSource.setTimeBetweenLogStatsMillis(300000);
        //druidDataSource.set
        return druidDataSource;
    }
    // 其中 dataSource 框架会自动为我们注入
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
