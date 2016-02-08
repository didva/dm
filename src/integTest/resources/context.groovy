import com.epam.trainings.spring.core.dm.dao.impl.inmemory.*
import com.epam.trainings.spring.core.dm.service.impl.*
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy
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
    ticketDao(TicketDaoInMemoryImpl)
    userDao(UserDaoInMemoryImpl)
    // DAOs end

    // Services start
    userService(UserServiceImpl) {
        userDao = ref('userDao')
        ticketDao = ref('ticketDao')
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
        ticketDao = ref('ticketDao')
    }
    discountService(DiscountServiceImpl) {
        strategies = [ref('nthMultipleTicketDiscountStrategy'), ref('birthdayDiscountStrategy')]
    }
    bookingService(BookingServiceImpl) {
        discountService = ref('discountService')
        ticketDao = ref('ticketDao')
        assignedEventsDao = ref('assignedEventsDao')
        auditoriumService = ref('auditoriumService')
    }
    eventServive(EventServiceImpl) {
        eventDao = ref('eventDao')
        assignedEventsDao = ref('assignedEventsDao')
    }
    // Services end
}
