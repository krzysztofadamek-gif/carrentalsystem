package com.carrentalsystem;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import jakarta.transaction.UserTransaction;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class ReservationResourcesTest {

    @Inject
    UserTransaction ut;

    @BeforeEach
    void setup() throws Exception { // Add throws Exception for UserTransaction methods
        ut.begin(); // Start a new transaction
        // Clear all existing cars and reservations to ensure a clean state for each test
        Reservation.deleteAll();
        Car.deleteAll();

        // Populate with initial cars for testing
        // Directly persist cars instead of calling the REST endpoint in tests for better control
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
        ut.commit(); // Commit the transaction
    }

    @Test
    void reservationSuccessful() {
        LocalDate startDate = LocalDate.now();
        ReservationWithVerificationRequest request = new ReservationWithVerificationRequest(CarType.SEDAN, startDate, 3);
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservationSystem/reservations/addReservationWithVerification")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("car.carType", is(CarType.SEDAN.toString()))
                .body("startDate", is(startDate.toString()))
                .body("endDate", is(startDate.plusDays(3).toString()));
    }

    @Test
    void reservationFailedLackOfCarsOfSpecificType() throws Exception { // Add throws Exception
        ut.begin(); // Start a new transaction for this test's setup
        // Delete all SEDAN cars to simulate lack of specific type
        Car.delete("carType", CarType.SEDAN);
        ut.commit(); // Commit the transaction

        LocalDate startDate = LocalDate.now().plusDays(1);
        ReservationWithVerificationRequest request = new ReservationWithVerificationRequest(CarType.SEDAN, startDate, 3);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservationSystem/reservations/addReservationWithVerification")
                .then()
                .statusCode(404)
                .body(is("No available car of type SEDAN for the requested period."));
    }

    @Test
    void reservationFailedLackOfFreeCarsOfSpecificType() throws Exception { // Add throws Exception
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(5);

        ut.begin(); // Start a new transaction for this test's setup
        // Block all SEDAN cars by making reservations for them
        // We know initialCarSetupForTesting creates 3 SEDANs
        Car.list("carType", CarType.SEDAN).stream().limit(3).forEach(car -> {
            Reservation reservation = new Reservation((Car) car, startDate, endDate);
            reservation.persist();
        });
        ut.commit(); // Commit the transaction

        // Attempt to make another reservation for a SEDAN during the same period
        ReservationWithVerificationRequest request = new ReservationWithVerificationRequest(CarType.SEDAN, startDate, 3);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservationSystem/reservations/addReservationWithVerification")
                .then()
                .statusCode(404)
                .body(is("No available car of type SEDAN for the requested period."));
    }
}
