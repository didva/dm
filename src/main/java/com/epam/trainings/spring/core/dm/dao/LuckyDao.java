package com.epam.trainings.spring.core.dm.dao;

import com.epam.trainings.spring.core.dm.model.LuckyInfo;

import java.util.List;

public interface LuckyDao {

    void register(long ticketId, long userId);

    LuckyInfo findByTicketId(long ticketId);

    List<LuckyInfo> findByUserId(long userId);

}
