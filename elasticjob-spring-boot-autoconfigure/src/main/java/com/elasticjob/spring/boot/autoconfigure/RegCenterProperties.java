package com.elasticjob.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * created by xiao.lizi 2020/5/26 16:05
 */
@ConfigurationProperties(prefix = "elasticjob.regCenter")
public class RegCenterProperties {
    private String serverList;

    private String namespace;

    public String getServerList() {
        return this.serverList;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}

