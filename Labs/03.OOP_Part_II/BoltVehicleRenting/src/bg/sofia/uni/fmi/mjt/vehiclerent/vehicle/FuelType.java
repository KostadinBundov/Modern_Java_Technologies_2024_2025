package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType {
    DIESEL(3),
    PETROL(3),
    HYBRID(1),
    ELECTRICITY(0),
    HYDROGEN(0);

    private final double dailyFee;

    FuelType(double dailyFee) {
        this.dailyFee = dailyFee;
    }

    public double getDailyFee() {
        return dailyFee;
    }
}