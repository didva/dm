import com.epam.trainings.spring.core.dm.aspects.EventsStatisticAspect
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.*
import com.epam.trainings.spring.core.dm.service.impl.*
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
import org.springframework.core.io.ClassPathResource

beans {

    // Properties start
    def properties = new Properties()
    properties.load(new ClassPathResource('config.properties').inputStream);
    // Properties end

    // DAOs start
    assignedEventsDao(AssignedEventsDaoInMemoryImpl)
    auditoriumDao(AuditoriumDaoInMemoryImpl, properties.get("auditorium.properties.path"))
    eventDao(EventDaoInMemoryImpl)
    ticketsDao(TicketsDaoInMemoryImpl)
    userDao(UserDaoInMemoryImpl)
    eventByNameAccessionsCounterDao(GeneralEventCounterDaoInMemoryImpl)
    eventPriceCalculationsCounterDao(GeneralEventCounterDaoInMemoryImpl)
    eventTicketsBookingsCounterDao(GeneralEventCounterDaoInMemoryImpl)
    // DAOs end

    // Services start
    userService(UserServiceImpl) {
        userDao = ref('userDao')
        ticketsDao = ref('ticketsDao')
    }
    auditoriumService(AuditoriumServiceImpl) {
        auditoriumDao = ref('auditoriumDao')
    }
    birthdayDiscountStrategy(BirthdayDiscountStrategy) {
        discountPercentage = properties.get "discount.birthday.discountPercentage"
    }
    nthMultipleTicketDiscountStrategy(NthMultipleTicketDiscountStrategy) {
        discountPercentage = properties.get "discount.nthTicket.discountPercentage"
        ticketsToDiscount = properties.get "discount.nthTicket.ticketsToDiscount"
        ticketsDao = ref('ticketsDao')
    }
    discountService(DiscountServiceImpl) {
        strategies = [ref('nthMultipleTicketDiscountStrategy'), ref('birthdayDiscountStrategy')]
    }
    eventService(EventServiceImpl) {
        eventDao = ref('eventDao')
        assignedEventsDao = ref('assignedEventsDao')
    }
    bookingService(BookingServiceImpl) {
        discountService = ref('discountService')
        ticketsDao = ref('ticketsDao')
        eventService = ref('eventService')
        auditoriumService = ref('auditoriumService')
    }
    eventsStatisticService(EventsStatisticServiceImpl) {
        eventByNameAccessionsCounterDao = ref('eventByNameAccessionsCounterDao')
        eventPriceCalculationsCounterDao = ref('eventPriceCalculationsCounterDao')
        eventTicketsBookingsCounterDao = ref('eventTicketsBookingsCounterDao')
    }
    // Services end

    // Aspects start
    autoProxyCreator(AnnotationAwareAspectJAutoProxyCreator) {
        proxyTargetClass = false
    }
    eventsStatiscticApect(EventsStatisticAspect) {
        eventsStatisticService = ref('eventsStatisticService')
    }
    // Aspects end
}
