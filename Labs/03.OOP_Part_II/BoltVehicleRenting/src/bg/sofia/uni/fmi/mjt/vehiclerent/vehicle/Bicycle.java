package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics.ShortRentVehicleCharacteristics;

import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;

public final class Bicycle extends Vehicle {
    private ShortRentVehicleCharacteristics vehicleCharacteristics;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        vehicleCharacteristics = new ShortRentVehicleCharacteristics(pricePerHour, pricePerDay);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("End date is before start date!");
        }

        if(!validateMinPeriodForRent(startOfRent, endOfRent)) {
            throw  new InvalidRentingPeriodException("Minimum or maximum rental time for the bike is not in range!");
        }

        long totalHours = calculateHoursBetweenTwoDates(startOfRent, endOfRent);

        if(totalHours < 1) {
            totalHours = 1;
        }

        long days = (totalHours % 168) / 24;
        long hours = totalHours % 24;

        return (days * vehicleCharacteristics.getPricePerDay())
                + (hours * vehicleCharacteristics.getPricePerHour());
    }

    @Override
    public boolean validateMinPeriodForRent(LocalDateTime rentalStart, LocalDateTime rentalEnd) {
        long daysBetween = rentalStart.until(rentalEnd, DAYS);

        return daysBetween < 7;
    }
}