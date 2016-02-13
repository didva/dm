package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.aspects.DiscountsStatisticAspect;
import com.epam.trainings.spring.core.dm.aspects.EventsStatisticAspect;
import com.epam.trainings.spring.core.dm.aspects.LuckyAspect;
import com.epam.trainings.spring.core.dm.service.RandomGeneratorService;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableAspectJAutoProxy
public class AppAspectConfig {

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private RandomGeneratorService randomGeneratorService;

    @Bean
    @Scope(value = "prototype")
    public Boolean isUserLucky() {
        return randomGeneratorService.getBoolean();
    }

    @Bean
    public EventsStatisticAspect eventsStatisticAspect() {
        EventsStatisticAspect eventsStatisticAspect = new EventsStatisticAspect();
        eventsStatisticAspect.setStatisticService(statisticService);
        return eventsStatisticAspect;
    }

    @Bean
    public DiscountsStatisticAspect discountsStatisticAspect() {
        DiscountsStatisticAspect discountsStatisticAspect = new DiscountsStatisticAspect();
        discountsStatisticAspect.setStatisticService(statisticService);
        return discountsStatisticAspect;
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
