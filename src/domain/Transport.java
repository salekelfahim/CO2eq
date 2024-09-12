package domain;

import java.time.LocalDate;

public class Transport extends Consomation{
    private double distance;
    private TypeVehicule type;

    public Transport(int value, LocalDate startDate, LocalDate endDate, double distance, TypeVehicule type) {
        super(value, startDate, endDate);
        this.distance = distance;
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public TypeVehicule getTypeV() {
        return type;
    }

    public void setType(TypeVehicule type) {
        this.type = type;
    }

    @Override
    public Double calculImpact() {
        return null;
    }
}