package com.epam.trainings.spring.core.dm.aspects;

import com.epam.trainings.spring.core.dm.model.Ticket;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public abstract class LuckyAspect {

    private StatisticService statisticService;

    @Around(value = "execution(* com.epam.trainings.spring.core.dm.dao.TicketsDao.add(..)) && args(user, ticket)",
            argNames = "pjp,user,ticket")
    public Object saveStatistics(ProceedingJoinPoint pjp, User user, Ticket ticket) throws Throwable {
        Boolean isLucky = isLucky();
        if (user != null && isLucky) {
            ticket.setFinalPrice(0);
        }
        Object result = pjp.proceed(new Object[]{user, ticket});
        if (user != null && isLucky) {
            statisticService.userIsLucky(user.getId(), ticket.getId());
        }
        return result;
    }

    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    protected abstract Boolean isLucky();
}
