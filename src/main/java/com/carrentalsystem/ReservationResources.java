package com.carrentalsystem;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Path("reservationSystem")
public class ReservationResources {
    @Path("/reservations")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllReservations() {
        List<Reservation> allReservations = Reservation.listAll();
        StringBuilder ret = new StringBuilder();
        for(Reservation reservation : allReservations){
            ret.append(reservation.toString()).append("\n");
        }
        return ret.toString();
    }

    @Path("/reservations/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addReservation(ReservationRequest reservationRequest) {
        Car car = Car.findById(reservationRequest.carId);
        if (car == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Car with ID " + reservationRequest.carId + " not found").build();
        }

        Reservation reservation = new Reservation(car, reservationRequest.startDate, reservationRequest.endDate);
        reservation.persist();
        return Response.ok(reservation).build();
    }

    @Path("/reservations/addReservationWithVerification")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addReservationWithVerification(ReservationWithVerificationRequest request) {
        LocalDate endDate = request.startDate.plusDays(request.numberOfDays);

        // Find available cars of the specified type
        List<Car> availableCars = Car.list("carType", request.carType);

        Optional<Car> carForReservation = availableCars.stream()
                .filter(car -> {
                    // Check if the car has any overlapping reservations
                    List<Reservation> overlappingReservations = Reservation.list(
                            "car = ?1 and ((startDate <= ?2 and endDate >= ?2) or (startDate <= ?3 and endDate >= ?3) or (startDate >= ?2 and endDate <= ?3))",
                            car, request.startDate, endDate
                    );
                    return overlappingReservations.isEmpty();
                })
                .findFirst();

        if (carForReservation.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No available car of type " + request.carType + " for the requested period.").build();
        }

        Reservation reservation = new Reservation(carForReservation.get(), request.startDate, endDate);
        reservation.persist();
        return Response.ok(reservation).build();
    }

}
