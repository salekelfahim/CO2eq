package domain;

import java.time.LocalDate;

public class Logement extends Consomation{

    private TypeEnergie typeE;
    private Double energie;


    public Logement(int value, LocalDate startDate, LocalDate endDate, TypeEnergie typeEnergie, Double energie, int type) {
        super(value, startDate, endDate, type);
        this.typeE = typeE;
        this.energie = energie;
    }

    public TypeEnergie getTypeE() {
        return typeE;
    }

    public void setType(TypeEnergie type) {
        this.typeE = typeE;
    }

    public Double getEnergie() {
        return energie;
    }

    public void setEnergie(Double consommationEnergie) {
        this.energie = energie;
    }

    @Override
    public Double calculImpact() {
        return null;
    }
}
