package com.epam.trainings.spring.core.dm.service.impl

import com.epam.trainings.spring.core.dm.Utils
import com.epam.trainings.spring.core.dm.dao.AuditoriumDao
import org.junit.Before
import org.junit.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class TestAuditoriumServiceImpl {

    AuditoriumServiceImpl auditoriumService;
    AuditoriumDao auditoriumDao;

    @Before
    void init() {
        auditoriumDao = mock AuditoriumDao.class

        auditoriumService = AuditoriumServiceImpl.newInstance()
        auditoriumService.setAuditoriumDao auditoriumDao
    }

    @Test
    void testGetAuditoriums() {
        def auditoriums = [Utils.createAuditorium("name1", 1, []), Utils.createAuditorium("name2", 1, [])]
        when(auditoriumDao.findAll()).thenReturn auditoriums

        assert auditoriums == auditoriumService.getAuditoriums()
    }

    @Test
    void testGetAuditorium() {
        def name = "name1"
        def auditorium = Utils.createAuditorium(name, 1, [])
        when(auditoriumDao.findByName(name)).thenReturn auditorium

        assert auditorium == auditoriumService.getAuditorium(name)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetAuditoriumIllegalName() {
        auditoriumService.getAuditorium null
    }

}
