package com.carrentalsystem;

import java.time.LocalDate;

public class ReservationRequest {
    public Long carId;
    public LocalDate startDate;
    public LocalDate endDate;

    public ReservationRequest() {
    }

    public ReservationRequest(Long carId, LocalDate startDate, LocalDate endDate) {
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
