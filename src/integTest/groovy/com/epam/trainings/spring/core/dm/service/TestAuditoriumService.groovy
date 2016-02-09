package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.model.Auditorium
import org.junit.Before
import org.junit.Test
import org.springframework.context.support.GenericGroovyApplicationContext

import static com.epam.trainings.spring.core.dm.Utils.createAuditorium

class TestAuditoriumService {

    Auditorium auditorium1
    Auditorium auditorium2
    AuditoriumService auditoriumService

    @Before
    void init() {
        def context = new GenericGroovyApplicationContext("classpath:context.groovy");

        auditorium1 = createAuditorium "testAuditoriumB", 15, [11, 12, 13, 14, 15]
        auditorium2 = createAuditorium "testAuditoriumC", 10, [1, 2, 3, 4, 5]
        auditoriumService = context.getBean AuditoriumService
    }

    @Test
    void testGetAuditoriums() {
        def actual = auditoriumService.getAuditoriums()
        assert actual.containsAll([auditorium1, auditorium2])
    }

    @Test
    void testGetAuditorium() {
        assert auditorium1 == auditoriumService.getAuditorium(auditorium1.name)
        assert auditorium2 == auditoriumService.getAuditorium(auditorium2.name)
    }
}
