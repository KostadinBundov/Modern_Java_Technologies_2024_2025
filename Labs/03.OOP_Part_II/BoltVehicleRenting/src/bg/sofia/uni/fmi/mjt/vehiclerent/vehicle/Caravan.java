package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics.LongRentVehicleCharacteristics;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics.OvernightVehicleCharacteristics;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

public final class Caravan extends Vehicle {
    private static final int MIN_DAYS_FOR_RENTING = 1;
    private OvernightVehicleCharacteristics vehicleCharacteristics;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        vehicleCharacteristics = new OvernightVehicleCharacteristics(pricePerWeek, pricePerDay, pricePerHour, fuelType, numberOfSeats, numberOfBeds);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("End date is before start date!");
        }

        if(!validateMinPeriodForRent(startOfRent, endOfRent)) {
            throw  new InvalidRentingPeriodException("Minimum or maximum rental time for the caravan is not in range!");
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
        totalPrice += vehicleCharacteristics.getNumberOfBeds() * OvernightVehicleCharacteristics.PRICE_PER_BED;

        return totalPrice + driver.group().getDriverFee();
    }

    @Override
    public boolean validateMinPeriodForRent(LocalDateTime rentalStart, LocalDateTime rentalEnd) {
        long daysBetween = rentalStart.until(rentalEnd, DAYS);

        return daysBetween >= 1;
    }
}