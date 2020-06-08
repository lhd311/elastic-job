package com.elasticjob.spring.boot.autoconfigure;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * created by xiao.lizi 2020/5/26 16:02
 */
public class ElasticConfigHelper {

    @Resource
    private ZookeeperRegistryCenter regCenter;

    @Resource
    private JobEventConfiguration jobEventConfiguration;

    private LiteJobConfiguration getSimpleJobConfiguration(Class<? extends SimpleJob> jobClass, String cron, int shardingTotalCount, String shardingItemParameters) {
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build();
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());
        return LiteJobConfiguration.newBuilder((JobTypeConfiguration)simpleJobConfiguration).overwrite(true).build();
    }

    private LiteJobConfiguration getDataflowConfiguration(Class<? extends DataflowJob> jobClass, String cron, int shardingTotalCount, String shardingItemParameters, boolean streamingProcess) {
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build();
        DataflowJobConfiguration simpleJobConfiguration = new DataflowJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName(), streamingProcess);
        return LiteJobConfiguration.newBuilder((JobTypeConfiguration)simpleJobConfiguration).overwrite(true).build();
    }

    public void initSimpleJobScheduler(SimpleJob simpleJob, String cron, int shardingTotalCount, String shardingItemParameters) {
        (new SpringJobScheduler((ElasticJob)simpleJob, (CoordinatorRegistryCenter)this.regCenter, getSimpleJobConfiguration((Class)simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters), this.jobEventConfiguration, new com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener[0]))
                .init();
    }

    public void initDataflowScheduler(DataflowJob dataflowJob, String cron, int shardingTotalCount, String shardingItemParameters, boolean streamingProcess) {
        (new SpringJobScheduler((ElasticJob)dataflowJob, (CoordinatorRegistryCenter)this.regCenter, getDataflowConfiguration((Class)dataflowJob.getClass(), cron, shardingTotalCount, shardingItemParameters, streamingProcess), this.jobEventConfiguration, new com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener[0]))
                .init();
    }
}
