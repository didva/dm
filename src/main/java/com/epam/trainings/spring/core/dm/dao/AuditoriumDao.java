package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.Auditorium;

import java.util.List;

public interface AuditoriumDao {

    List<Auditorium> findAll();

    Auditorium findByName(String name);

}
