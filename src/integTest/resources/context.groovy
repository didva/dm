import com.epam.trainings.spring.core.dm.aspects.DiscountsStatisticAspect
import com.epam.trainings.spring.core.dm.aspects.EventsStatisticAspect
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.AuditoriumDaoInMemoryImpl
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.DiscountCounterDaoInMemoryImpl
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.GeneralEventCounterDaoInMemoryImpl
import com.epam.trainings.spring.core.dm.dao.impl.inmemory.LuckyDaoInMemoryImpl
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.AssignedEventsJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.DiscountCounterJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.EventJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.GeneralEventCounterJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.LuckyJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.TicketsJdbcDao
import com.epam.trainings.spring.core.dm.dao.impl.spring.jdbc.UserJdbcDao
import com.epam.trainings.spring.core.dm.service.impl.*
import com.epam.trainings.spring.core.dm.service.impl.strategies.BirthdayDiscountStrategy
import com.epam.trainings.spring.core.dm.service.impl.strategies.NthMultipleTicketDiscountStrategy
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
import org.springframework.transaction.interceptor.TransactionInterceptor

beans {

    // Properties start
    def properties = new Properties()
    properties.load(new ClassPathResource('config.properties').inputStream);
    // Properties end

    //JDBC start
    def dataSourceBean = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
    def populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource('db/create-db.sql'))
    DatabasePopulatorUtils.execute(populator, dataSourceBean)

    transactionManager(DataSourceTransactionManager) {
        dataSource = dataSourceBean
    }
    transactionAttributeSource(AnnotationTransactionAttributeSource)
    transactionInterceptor(TransactionInterceptor) {
        transactionAttributeSource = ref('transactionAttributeSource')
        transactionManager = ref('transactionManager')
    }
    internalTransactionAdvisor(BeanFactoryTransactionAttributeSourceAdvisor) {
        transactionAttributeSource = ref('transactionAttributeSource')
        advice = ref('transactionInterceptor')
        order = Integer.MAX_VALUE
    }

    //JDBC end

    // DAOs start
    auditoriumDao(AuditoriumDaoInMemoryImpl, properties.get("auditorium.properties.path"))
    userDao(UserJdbcDao) {
        dataSource = dataSourceBean
    }
    eventDao(EventJdbcDao) {
        dataSource = dataSourceBean
    }
    assignedEventsDao(AssignedEventsJdbcDao) {
        dataSource = dataSourceBean
    }
    ticketsDao(TicketsJdbcDao) {
        dataSource = dataSourceBean
    }
    luckyDao(LuckyJdbcDao) {
        dataSource = dataSourceBean
    }
    discountCounterDao(DiscountCounterJdbcDao) {
        dataSource = dataSourceBean
    }
    eventByNameAccessionsCounterDao(GeneralEventCounterJdbcDao) {
        tableName = 'events_by_name'
        dataSource = dataSourceBean
    }
    eventPriceCalculationsCounterDao(GeneralEventCounterJdbcDao) {
        tableName = 'events_price'
        dataSource = dataSourceBean
    }
    eventTicketsBookingsCounterDao(GeneralEventCounterJdbcDao) {
        tableName = 'events_tickets'
        dataSource = dataSourceBean
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
        eventService = ref('eventService')
        eventByNameAccessionsCounterDao = ref('eventByNameAccessionsCounterDao')
        eventPriceCalculationsCounterDao = ref('eventPriceCalculationsCounterDao')
        eventTicketsBookingsCounterDao = ref('eventTicketsBookingsCounterDao')
        discountCounterDao = ref("discountCounterDao")
        luckyDao = ref("luckyDao")
        ticketsDao = ref('ticketsDao')
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
