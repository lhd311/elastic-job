# elastic-job
1、打包成新jar进私服或本地仓库
2、添加依赖

       <dependency>
            <groupId>org.example</groupId>
            <artifactId>elasticjob-spring-boot-autoconfigure</artifactId>
            <version>1.0.0</version>
        </dependency>

3、配置文件添加配置
```
elasticjob:
  regCenter:
    server-list: zk1:2181,zk2:2181,zk3:2181
  datasource:
    url:localhost:3306/elasticjo?useUnicode=true&characterEncoding=utf-8&verifyServerCertificate=false&useSSL=false&requireSSL=false
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: root
    max-active: 10
    min-idle: 5
    initial-size: 5
    test-on-borrow: true
    validation-query: SELECT 1
```

datasource是可选项

4、job使用
```
import com.elasticjob.spring.boot.autoconfigure.ElasticConfigHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by xiao.lizi 2020/5/26 10:38
 */
@Configuration
public class SimpleJobConfig {
    @Autowired
    private ElasticConfigHelper elasticConfigHelper;


    @Bean
    public SpringBootJob simpleJobScheduler( @Value("${simpleJob.testJob.cron}") final String cron,
                                           @Value("${simpleJob.testJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${simpleJob.testJob.shardingItemParameters}") final String shardingItemParameters) {
        SpringBootJob springBootJob = new SpringBootJob();
        elasticConfigHelper.initSimpleJobScheduler( springBootJob, cron, shardingTotalCount, shardingItemParameters );
        return  springBootJob;
    }    
}

```
job类只要继承SimpleJob即可
```
public class SpringBootJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(">>>>>>>>>>>>>>>>>>>" + shardingContext + "<<<<<<<<<<<<<<<<<<<<<<");
    }
}
```
