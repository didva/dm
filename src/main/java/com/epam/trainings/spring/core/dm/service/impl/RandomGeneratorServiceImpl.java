package com.epam.trainings.spring.core.dm.service.impl;

import com.epam.trainings.spring.core.dm.service.RandomGeneratorService;

import java.util.Random;

public class RandomGeneratorServiceImpl implements RandomGeneratorService {
    @Override
    public Boolean getBoolean() {
        return new Random().nextBoolean();
    }
}
