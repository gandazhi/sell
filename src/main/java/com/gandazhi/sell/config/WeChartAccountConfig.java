package com.gandazhi.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechart")
@Data
public class WeChartAccountConfig {

    private String mpAppId;
    private String mpAppSecret;
}
