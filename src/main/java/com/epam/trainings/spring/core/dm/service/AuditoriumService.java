package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Auditorium;

import java.util.List;

public interface AuditoriumService {

    List<Auditorium> getAuditoriums();

    Auditorium getAuditorium(String name);

}
