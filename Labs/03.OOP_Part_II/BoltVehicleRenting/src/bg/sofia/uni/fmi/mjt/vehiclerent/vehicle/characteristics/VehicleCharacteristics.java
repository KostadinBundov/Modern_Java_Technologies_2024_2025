package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.characteristics;

public class VehicleCharacteristics {
    private final double pricePerHour;
    private final double pricePerDay;

    public VehicleCharacteristics(double pricePerHour, double pricePerDay) {
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }
}