package com.epam.trainings.spring.core.dm.service;

import com.epam.trainings.spring.core.dm.model.Counter;
import com.epam.trainings.spring.core.dm.model.LuckyInfo;

import java.util.List;

public interface StatisticService {

    void increaseDiscounts(String discountName, long userId);

    void increaseRequestsByName(long eventId);

    void increaseRequestsForPrices(long eventId);

    void increaseBookedTimes(long eventId);

    Counter<Long> getRequestsByName(long eventId);

    Counter<Long> getRequestsForPrices(long eventId);

    Counter<Long> getBookedTimes(long eventId);

    List<Counter<String>> getAllDiscounts();

    List<Counter<String>> getDiscountsByUser(long userId);

    void userIsLucky(long userId, long ticketId);

    List<LuckyInfo> getLuckyInfoByUserId(long userId);

    List<LuckyInfo> getLuckyInfoByEventId(long eventId);

}
