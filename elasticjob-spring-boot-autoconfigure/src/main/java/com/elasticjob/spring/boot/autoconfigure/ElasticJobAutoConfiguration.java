package com.elasticjob.spring.boot.autoconfigure;


import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * created by xiao.lizi 2020/5/26 16:12
 */
@Configuration
@Import({ElasticConfigHelper.class})
@EnableConfigurationProperties({RegCenterProperties.class, ElasticJobEventRdbProperties.class})
public class ElasticJobAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ElasticJobAutoConfiguration.class);

    @Bean
    public JobEventConfiguration jobEventConfiguration(ElasticJobEventRdbProperties properties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setMaxActive(properties.getMaxActive());
        dataSource.setMinIdle(properties.getMinIdle());
        dataSource.setInitialSize(properties.getInitialSize());
        dataSource.setTestOnBorrow(properties.getTestOnBorrow());
        dataSource.setValidationQuery(properties.getValidationQuery());
        dataSource.setConnectionProperties(properties.getConnectionProperties());
        try {
            dataSource.setFilters(properties.getFilters());
            dataSource.init();
        } catch (Exception e) {
            logger.error("ElasticJob Starter DruidDataSource init Exception e={}", e.getMessage());
            e.printStackTrace();
        }
        return (JobEventConfiguration)new JobEventRdbConfiguration((DataSource)dataSource);
    }

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(RegCenterProperties regCenterProperties) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(regCenterProperties.getServerList(), regCenterProperties.getNamespace()));
    }
}
