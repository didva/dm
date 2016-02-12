package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.LuckyInfo;

import java.util.List;

public interface LuckyDao {

    void register(long ticketId, long userId);

    List<LuckyInfo> findByEventId(long eventId);

    List<LuckyInfo> findByUserId(long userId);

}
