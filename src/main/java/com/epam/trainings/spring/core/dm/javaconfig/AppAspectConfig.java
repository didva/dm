package com.epam.trainings.spring.core.dm.javaconfig;

import com.epam.trainings.spring.core.dm.aspects.EventsStatisticAspect;
import com.epam.trainings.spring.core.dm.service.EventsStatisticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppAspectConfig {

    @Autowired
    private EventsStatisticService eventsStatisticService;


    @Bean
    public EventsStatisticAspect eventsStatisticAspect() {
        EventsStatisticAspect eventsStatisticAspect = new EventsStatisticAspect();
        eventsStatisticAspect.setEventsStatisticService(eventsStatisticService);
        return eventsStatisticAspect;
    }
}
