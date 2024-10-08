package domain;

import java.time.LocalDate;

public class Alimentation extends Consomation{
    private Double poids;
    private  TypeAliment typeA;

    public Alimentation(int value, LocalDate startDate, LocalDate endDate, Double poids, TypeAliment typeA, int type) {
        super(value, startDate, endDate, type);
        this.poids = poids;
        this.typeA = typeA;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

public TypeAliment getTypeA() {
        return typeA;
    }

    public void setTypeAliment(TypeAliment typeAliment) {
        this.typeA = typeA;
    }

    @Override
    public Double calculImpact() {
        if (typeA == TypeAliment.VIANDE) {
            return poids * 5.0;
        } else if (typeA == TypeAliment.LEGUME) {
            return poids * 0.5;
        }
        return 0.0;
    }}
