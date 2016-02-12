package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.dao.EventCounterDao
import com.epam.trainings.spring.core.dm.model.Counter
import org.junit.Before
import org.junit.Test

import static org.mockito.Mockito.*

class TestEventsStatisticServiceImpl {

    EventsStatisticServiceImpl eventsStatisticService

    EventCounterDao eventByNameAccessionsCounterDao
    EventCounterDao eventPriceCalculationsCounterDao
    EventCounterDao eventTicketsBookingsCounterDao

    @Before
    void init() {
        eventByNameAccessionsCounterDao = mock EventCounterDao
        eventPriceCalculationsCounterDao = mock EventCounterDao
        eventTicketsBookingsCounterDao = mock EventCounterDao

        eventsStatisticService = new EventsStatisticServiceImpl()
        eventsStatisticService.setEventByNameAccessionsCounterDao eventByNameAccessionsCounterDao
        eventsStatisticService.setEventPriceCalculationsCounterDao eventPriceCalculationsCounterDao
        eventsStatisticService.setEventTicketsBookingsCounterDao eventTicketsBookingsCounterDao
    }

    @Test
    void testIncreaseRequestsByName() {
        def eventId = 1
        eventsStatisticService.increaseRequestsByName eventId
        verify(eventByNameAccessionsCounterDao, times(1)).increase(eventId)
    }

    @Test
    void testIncreaseRequestsForPrices() {
        def eventId = 1
        eventsStatisticService.increaseRequestsForPrices eventId
        verify(eventPriceCalculationsCounterDao, times(1)).increase(eventId)
    }

    @Test
    void testIncreaseBookedTimes() {
        def eventId = 1
        eventsStatisticService.increaseBookedTimes eventId
        verify(eventTicketsBookingsCounterDao, times(1)).increase(eventId)
    }

    @Test
    void testGetRequestsByName() {
        def eventId = 1, counter = new Counter(eventId, 10)
        when(eventByNameAccessionsCounterDao.findByEventId(eventId)).thenReturn counter
        assert counter == eventsStatisticService.getRequestsByName(eventId)

    }

    @Test
    void testGetRequestsForPrices() {
        def eventId = 1, counter = new Counter(eventId, 10)
        when(eventPriceCalculationsCounterDao.findByEventId(eventId)).thenReturn counter
        assert counter == eventsStatisticService.getRequestsForPrices(eventId)
    }

    @Test
    void testGetBookedTimes() {
        def eventId = 1, counter = new Counter(eventId, 10)
        when(eventTicketsBookingsCounterDao.findByEventId(eventId)).thenReturn counter
        assert counter == eventsStatisticService.getBookedTimes(eventId)
    }


}
