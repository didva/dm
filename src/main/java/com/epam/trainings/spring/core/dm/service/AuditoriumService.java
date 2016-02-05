package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Auditorium;

import java.util.List;
import java.util.Set;

public interface AuditoriumService {

    List<Auditorium> getAuditoriums();

    int getSeatsNumber(long id);

    Set<Integer> getVipSeats(long id);

}
