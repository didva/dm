package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.dao.TicketDao
import com.epam.trainings.spring.core.dm.dao.UserDao
import com.epam.trainings.spring.core.dm.exceptions.service.AlreadyExistsException
import com.epam.trainings.spring.core.dm.model.User
import org.junit.Before
import org.junit.Test

import java.time.LocalDate

import static com.epam.trainings.spring.core.dm.Utils.createTicket
import static org.mockito.Mockito.*

class TestUserServiceImpl {

    UserServiceImpl userService;
    User user;
    UserDao userDao
    TicketDao ticketDao

    @Before
    void init() {
        userDao = mock(UserDao.class)
        ticketDao = mock(TicketDao.class)
        userService = UserServiceImpl.newInstance()
        userService.userDao = userDao;
        userService.ticketDao = ticketDao;

        user = Utils.createUser(0, "test_name", "test_email", LocalDate.now());
    }

    @Test
    void testRegisterUser() {
        userService.register(user)
        verify(userDao, times(1)).add(user)
    }

    @Test(expected = AlreadyExistsException.class)
    void testRegisterUserWithSameEmail() {
        when(userDao.findByEmail(user.email)).thenReturn(user)

        userService.register(user)
    }

    @Test(expected = IllegalArgumentException.class)
    void testRegisterWithIncorrectArgument() {
        userService.register(null)
    }

    @Test(expected = IllegalArgumentException.class)
    void testRegisterWithEmptyEmail() {
        user.email = null
        userService.register(user)
    }

    @Test(expected = IllegalArgumentException.class)
    void testRegisterWithEmptyName() {
        user.name = null
        userService.register(user)
    }

    @Test
    void testDeleteUser() {
        final long id = 1
        when(userDao.find(id)).thenReturn user

        userService.remove(id)
        verify(userDao, times(1)).delete(id)
    }

    @Test(expected = IllegalArgumentException.class)
    void testDeleteUnexistingUser() {
        final long id = 1
        when(userDao.find(id)).thenReturn(null)

        userService.remove(id)
    }

    @Test
    void testGetById() {
        final long id = 1
        when(userDao.find(id)).thenReturn(user)

        assert user == userService.getById(id)
    }

    @Test
    void testGetUserByEmail() {
        final String email = "some@email.com"
        when(userDao.findByEmail(email)).thenReturn(user)

        assert user == userService.getUserByEmail(email)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetUserByEmailEmptyEmail() {
        userService.getUserByEmail(null)
    }

    @Test
    void testGetUsersByName() {
        final String name = "test_name"
        def users = [user, Utils.createUser(2, "test_name", "email2", LocalDate.now())]
        when(userDao.findByName(name)).thenReturn(users)

        assert users == userService.getUsersByName(name)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetUsersByNameEmptyName() {
        userService.getUsersByName(null)
    }

    @Test
    void testGetBookedTickets() {
        final long userId = 1
        def tickets = [createTicket(1, 1, null, null, null, 0), createTicket(2, 1, null, null, null, 0)]
        when(userDao.find(userId)).thenReturn(user)
        when(ticketDao.findByUserId(userId)).thenReturn(tickets)

        assert tickets == userService.getBookedTickets(userId)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetBookedTicketsUnexistingUser() {
        final long userId = 1
        when(userDao.find(userId)).thenReturn(null)

        userService.getBookedTickets(userId)
    }
}
