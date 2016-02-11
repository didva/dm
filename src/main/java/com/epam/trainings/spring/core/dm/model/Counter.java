package com.epam.trainings.spring.core.dm.model;

public class Counter<T> {

    private T itemId;
    private int times;

    public T getItemId() {
        return itemId;
    }

    public void setItemId(T itemId) {
        this.itemId = itemId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
