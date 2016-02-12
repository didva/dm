package com.epam.trainings.spring.core.dm.service

import com.epam.trainings.spring.core.dm.model.Auditorium
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import static com.epam.trainings.spring.core.dm.Utils.createAuditorium

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:context.groovy")
class TestAuditoriumService {

    Auditorium auditorium1
    Auditorium auditorium2

    @Autowired
    AuditoriumService auditoriumService

    @Before
    void init() {
        auditorium1 = createAuditorium "testAuditoriumB", 15, [11, 12, 13, 14, 15]
        auditorium2 = createAuditorium "testAuditoriumC", 10, [1, 2, 3, 4, 5]
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
