package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics;

import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;

public class LongRentVehicleCharacteristics extends ShortRentVehicleCharacteristics {
    public static final double PRICE_PER_SEAT = 5;
    private final double pricePerWeek;
    private final FuelType fuelType;
    private final int numberOfSeats;

    public LongRentVehicleCharacteristics(double pricePerWeek, double pricePerDay, double pricePerHour, FuelType fuelType, int numberOfSeats) {
        super(pricePerHour, pricePerDay);
        this.pricePerWeek = pricePerWeek;
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
    }

    public double getPricePerWeek() {
        return pricePerWeek;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}