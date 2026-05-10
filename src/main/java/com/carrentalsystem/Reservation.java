package com.carrentalsystem;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Reservation extends PanacheEntity {

    @ManyToOne
    private Car car; // Make private
    private LocalDate startDate; // Make private
    private LocalDate endDate; // Make private

    public Reservation() {
    }

    public Reservation(Car car, LocalDate startDate, LocalDate endDate) {
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "car=" + (car != null ? car.id : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", id=" + id +
                '}';
    }
}