package com.carrentalsystem;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Car extends PanacheEntity {
    // Make fields private and add getters/setters
    private String name;
    private CarType carType;

    // Add default constructor for Panache (already present)
    public Car() {
    }

    public Car(String name, CarType carType) {
        this.name = name;
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", carType=" + carType +
                ", id=" + id +
                '}';
    }
}
