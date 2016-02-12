package com.epam.trainings.spring.core.dm.aspects;

import com.epam.trainings.spring.core.dm.model.AssignedEvent;
import com.epam.trainings.spring.core.dm.model.Event;
import com.epam.trainings.spring.core.dm.service.EventsStatisticService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class EventsStatisticAspect {

    private EventsStatisticService eventsStatisticService;

    @AfterReturning(value = "execution(* com.epam.trainings.spring.core.dm.service.EventService.getByName(..))", returning = "event")
    public void beforeEventByNameAccessed(Event event) {
        if (event != null) {
            eventsStatisticService.increaseRequestsByName(event.getId());
        }
    }

    @Before("execution(* com.epam.trainings.spring.core.dm.service.BookingService.getTicketPrice(..))")
    public void beforePriceWereQueried(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            AssignedEvent assignedEvent = (AssignedEvent) args[0];
            if (assignedEvent != null) {
                eventsStatisticService.increaseRequestsForPrices(assignedEvent.getEventId());
            }
        }
    }

    @AfterReturning("execution(* com.epam.trainings.spring.core.dm.service.BookingService.bookTicket(..))")
    public void afterBooking(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            AssignedEvent assignedEvent = (AssignedEvent) args[0];
            if (assignedEvent != null) {
                eventsStatisticService.increaseBookedTimes(assignedEvent.getId());
            }
        }
    }

    public void setEventsStatisticService(EventsStatisticService eventsStatisticService) {
        this.eventsStatisticService = eventsStatisticService;
    }
}
