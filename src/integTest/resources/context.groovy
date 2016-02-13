import com.epam.trainings.spring.core.dm.aspects.DiscountsStatisticAspect
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
    discountCounterDao(DiscountCounterDaoInMemoryImpl)
    luckyDao(LuckyDaoInMemoryImpl) {
        ticketsDao = ref('ticketsDao')
        assignedEventsDao = ref('assignedEventsDao')
    }
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
    statisticService(StatisticServiceImpl) {
        eventByNameAccessionsCounterDao = ref('eventByNameAccessionsCounterDao')
        eventPriceCalculationsCounterDao = ref('eventPriceCalculationsCounterDao')
        eventTicketsBookingsCounterDao = ref('eventTicketsBookingsCounterDao')
        discountCounterDao = ref("discountCounterDao")
        luckyDao = ref("luckyDao")
    }
    // Services end

    // Aspects start
    autoProxyCreator(AnnotationAwareAspectJAutoProxyCreator) {
        proxyTargetClass = false
    }
    eventsStatiscticApect(EventsStatisticAspect) {
        statisticService = ref('statisticService')
    }
    discountsStatisticAspect(DiscountsStatisticAspect) {
        statisticService = ref('statisticService')
    }
    // Aspects end
}
