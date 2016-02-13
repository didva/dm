package com.epam.trainings.spring.core.dm.aspects;

import com.epam.trainings.spring.core.dm.annotations.TrackDiscount;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Aspect
public class DiscountsStatisticAspect {

    private StatisticService statisticService;

    @Pointcut("@annotation(trackDiscount)")
    private void methodsAnnotatedWithTrackDiscount(TrackDiscount trackDiscount) {
    }

    @Pointcut(value = "execution(* com.epam.trainings.spring.core.dm.service.DiscountService.DiscountStrategy.getDiscount(..)) && args(user,event,dateTime)",
            argNames = "user,event,dateTime")
    private void methodsDiscountStrategiesMethod(User user, Event event, LocalDateTime dateTime) {
    }

    @AfterReturning(value = "methodsDiscountStrategiesMethod(user, com.epam.trainings.spring.core.dm.model.Event, java.time.LocalDateTime) && methodsAnnotatedWithTrackDiscount(trackDiscount)",
            returning = "discount", argNames = "user,discount,trackDiscount")
    public void saveStatistics(User user, double discount, TrackDiscount trackDiscount) {
        if (discount != 0 && user != null) {
            statisticService.increaseDiscounts(trackDiscount.name(), user.getId());
        }
    }

    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
