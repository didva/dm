package com.epam.trainings.spring.core.dm.model;

public class Event {

    private long id;
    private String name;
    private double price;
    private Rating rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if (id != event.id) {
            return false;
        }
        if (Double.compare(event.price, price) != 0) {
            return false;
        }
        if (name != null ? !name.equals(event.name) : event.name != null) {
            return false;
        }
        return rating == event.rating;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", rating=" + rating +
               '}';
    }
}
