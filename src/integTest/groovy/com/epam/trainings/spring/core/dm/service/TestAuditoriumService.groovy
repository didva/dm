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

        auditorium1 = createAuditorium "auditoriumB", 15, [11, 12, 13, 14, 15] as Set
        auditorium2 = createAuditorium "auditoriumC", 10, [1, 2, 3, 4, 5] as Set
        auditoriumService = context.getBean AuditoriumService
    }

    @Test
    void testGetAuditoriums() {
        def actual = auditoriumService.getAuditoriums()
        assert 2 == actual.size()
        assert [auditorium1, auditorium2].containsAll(actual)
    }

    @Test
    void testGetAuditorium() {
        assert auditorium1 == auditoriumService.getAuditorium(auditorium1.name)
        assert auditorium2 == auditoriumService.getAuditorium(auditorium2.name)
    }
}
