package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import com.epam.trainings.spring.core.dm.service.AuditoriumService;

import java.util.List;

public class AuditoriumServiceImpl implements AuditoriumService {

    private AuditoriumDao auditoriumDao;

    @Override
    public List<Auditorium> getAuditoriums() {
        return auditoriumDao.findAll();
    }

    @Override
    public Auditorium getAuditorium(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        return auditoriumDao.findByName(name);
    }

    public void setAuditoriumDao(AuditoriumDao auditoriumDao) {
        this.auditoriumDao = auditoriumDao;
    }
}
