package com.epam.trainings.spring.core.dm.config;

import com.epam.trainings.spring.core.dm.aspects.LuckyAspect;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;

@Configuration
@ImportResource("classpath:context.groovy")
public class TestLuckyConfig {

    @Autowired
    private StatisticService statisticService;

    @Bean
    @Scope(value = "prototype")
    public Boolean isUserLucky() {
        return true;
    }

    @Bean
    public LuckyAspect luckyAspect() {
        LuckyAspect luckyAspect = new LuckyAspect() {
            @Override
            protected Boolean isLucky() {
                return isUserLucky();
            }
        };
        luckyAspect.setStatisticService(statisticService);
        return luckyAspect;
    }
}
