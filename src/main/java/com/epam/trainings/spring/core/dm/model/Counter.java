package com.epam.trainings.spring.core.dm.model;

public class Counter<T> {

    private T itemId;
    private int times;

    public Counter() {
    }

    public Counter(T itemId, int times) {
        this.itemId = itemId;
        this.times = times;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Counter<?> counter = (Counter<?>) o;

        if (times != counter.times) {
            return false;
        }
        return !(itemId != null ? !itemId.equals(counter.itemId) : counter.itemId != null);

    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + times;
        return result;
    }
}
