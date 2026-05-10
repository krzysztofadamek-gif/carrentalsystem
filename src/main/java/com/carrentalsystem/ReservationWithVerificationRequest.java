package com.carrentalsystem;

import java.time.LocalDate;

public class ReservationWithVerificationRequest {
    public CarType carType;
    public LocalDate startDate;
    public int numberOfDays;

    public ReservationWithVerificationRequest() {
    }

    public ReservationWithVerificationRequest(CarType carType, LocalDate startDate, int numberOfDays) {
        this.carType = carType;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
    }
}
