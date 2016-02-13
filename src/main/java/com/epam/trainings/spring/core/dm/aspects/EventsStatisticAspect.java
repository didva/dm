package com.epam.trainings.spring.core.dm.aspects;

import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.model.User;
import com.epam.trainings.spring.core.dm.service.StatisticService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Set;

@Aspect
public class EventsStatisticAspect {

    private StatisticService statisticService;

    @AfterReturning(value = "execution(* com.epam.trainings.spring.core.dm.service.EventService.getByName(..))", returning = "event")
    public void beforeEventByNameAccessed(Event event) {
        if (event != null) {
            statisticService.increaseRequestsByName(event.getId());
        }
    }

    @Before(value = "execution(* com.epam.trainings.spring.core.dm.service.BookingService.getTicketPrice(..)) && args(assignedEvent, seats, user)", argNames = "assignedEvent,seats,user")
    public void beforePriceWereQueried(AssignedEvent assignedEvent, Set<Integer> seats, User user) {
        if (assignedEvent != null) {
            statisticService.increaseRequestsForPrices(assignedEvent.getEventId());
        }
    }

    @AfterReturning(value = "execution(* com.epam.trainings.spring.core.dm.service.BookingService.bookTicket(..))&& args(assignedEvent, seats, user)", argNames = "assignedEvent,seats,user")
    public void afterBooking(AssignedEvent assignedEvent, Set<Integer> seats, User user) {
        if (assignedEvent != null) {
            statisticService.increaseBookedTimes(assignedEvent.getId());
        }
    }

    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
