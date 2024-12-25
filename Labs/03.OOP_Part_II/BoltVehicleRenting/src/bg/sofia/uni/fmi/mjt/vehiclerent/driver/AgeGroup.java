package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10.0),
    EXPERIENCED(0.0),
    SENIOR(15.0);

    private final double youngDriverFee;

    AgeGroup(double youngDriverFee) {
        this.youngDriverFee = youngDriverFee;
    }

    public double getDriverFee() {
        return youngDriverFee;
    }
}