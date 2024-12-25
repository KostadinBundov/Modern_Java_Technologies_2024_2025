package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics;

import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;

public class OvernightVehicleCharacteristics extends LongRentVehicleCharacteristics {
    public static final double PRICE_PER_BED = 10;
    private int numberOfBeds;

    public OvernightVehicleCharacteristics(double pricePerWeek, double pricePerDay, double pricePerHour, FuelType fuelType, int numberOfSeats, int numberOfBeds) {
        super(pricePerWeek, pricePerDay, pricePerHour, fuelType, numberOfSeats);
        this.numberOfBeds = numberOfBeds;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }
}