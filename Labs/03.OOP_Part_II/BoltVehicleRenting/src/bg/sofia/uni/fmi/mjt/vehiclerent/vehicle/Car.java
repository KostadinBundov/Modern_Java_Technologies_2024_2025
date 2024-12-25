package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics.LongRentVehicleCharacteristics;

import java.time.LocalDateTime;

public final class Car extends Vehicle {
    private LongRentVehicleCharacteristics vehicleCharacteristics;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        vehicleCharacteristics = new LongRentVehicleCharacteristics(pricePerWeek, pricePerDay, pricePerHour,  fuelType, numberOfSeats);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("End date is before start date!");
        }

        long totalHours = calculateHoursBetweenTwoDates(startOfRent, endOfRent);

        long weeks = totalHours / 168;
        long days = (totalHours % 168) / 24;
        long hours = totalHours % 24;

        double totalPrice = (weeks * vehicleCharacteristics.getPricePerWeek())
                + (days * vehicleCharacteristics.getPricePerDay())
                + (hours * vehicleCharacteristics.getPricePerHour());

        totalPrice += (weeks * 7 + days) * vehicleCharacteristics.getFuelType().getDailyFee();
        totalPrice += vehicleCharacteristics.getNumberOfSeats() * LongRentVehicleCharacteristics.PRICE_PER_SEAT;

        return totalPrice + driver.group().getDriverFee();
    }

    @Override
    public boolean validateMinPeriodForRent(LocalDateTime rentalStart, LocalDateTime rentalEnd) {
        return true;
    }
}