package domain;

import java.time.LocalDate;

public class Transport extends Consomation{
    private double distance;
    private TypeVehicule typeV;

    public Transport(int value, LocalDate startDate, LocalDate endDate, double distance, TypeVehicule typeV, int type) {
        super(value, startDate, endDate, type);
        this.distance = distance;
        this.typeV = typeV;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public TypeVehicule getTypeV() {
        return typeV;
    }

    public void setType(TypeVehicule type) {
        this.typeV = typeV;
    }

    @Override
    public Double calculImpact() {
        if (typeV == TypeVehicule.VOITURE) {
            return distance * 0.5;
        } else if (typeV == TypeVehicule.TRAIN) {
            return distance * 0.1;
        }
        return 0.0;
    }
}