package com.carrentalsystem;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;

import java.util.List;

@Path("reservationSystem")
public class CarResources {

    @Path("cars")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCars() {
        List<Car> allCars = Car.listAll();
        StringBuilder ret = new StringBuilder();
        for(Car car : allCars){
            ret.append(car.toString()).append("\n");
        }
        return ret.toString();
    }

    @Path("cars/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Car addCars(Car car) {
        car.persist();
        return car;
    }

    @Path("cars/initialCarSetupForTesting")
    @POST
    @Transactional
    public void initialCarSetupForTesting() {
        new Car("Toyota Camry", CarType.SEDAN).persist();
        new Car("Honda CR-V", CarType.SUV).persist();
        new Car("Chrysler Pacifica", CarType.VAN).persist();
        new Car("Ford F-150", CarType.SUV).persist();
        new Car("BMW 3 Series", CarType.SEDAN).persist();
        new Car("Mercedes-Benz GLC", CarType.SUV).persist();
        new Car("Kia Carnival", CarType.VAN).persist();
        new Car("Nissan Altima", CarType.SEDAN).persist();
        new Car("Subaru Forester", CarType.SUV).persist();
        new Car("Dodge Grand Caravan", CarType.VAN).persist();
    }
}
