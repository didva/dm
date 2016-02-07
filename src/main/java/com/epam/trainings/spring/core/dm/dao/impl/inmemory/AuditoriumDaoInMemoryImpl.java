package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.model.Auditorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AuditoriumDaoInMemoryImpl implements AuditoriumDao {

    Set<Auditorium> auditoriumList;

    public AuditoriumDaoInMemoryImpl(Set<Auditorium> auditoriumList) {
        if (auditoriumList == null) {
            throw new IllegalArgumentException();
        }
        this.auditoriumList = auditoriumList;
    }

    @Override
    public List<Auditorium> findAll() {
        return new ArrayList<>(auditoriumList);
    }

    @Override
    public Auditorium findByName(String name) {
        return auditoriumList.stream().filter(auditorium -> auditorium.getName().equals(name)).findFirst().orElse(null);
    }


}
