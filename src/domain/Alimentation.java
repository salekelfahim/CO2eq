package domain;

import java.time.LocalDate;

public class Alimentation extends Consomation{
    private Double poids;
    private  TypeAliment type;

    public Alimentation(int value, LocalDate startDate, LocalDate endDate, Double poids, TypeAliment type) {
        super(value, startDate, endDate);
        this.poids = poids;
        this.type = type;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

public TypeAliment getTypeA() {
        return type;
    }

    public void setTypeAliment(TypeAliment typeAliment) {
        this.type = type;
    }

    @Override
    public Double calculImpact() {
        return null;
    }
}
