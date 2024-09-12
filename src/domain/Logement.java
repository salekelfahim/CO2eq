package domain;

import java.time.LocalDate;

public class Logement extends Consomation{

    private TypeEnergie type;
    private Double energie;


    public Logement(int value, LocalDate startDate, LocalDate endDate, TypeEnergie typeEnergie, Double energie) {
        super(value, startDate, endDate);
        this.type = type;
        this.energie = energie;
    }

    public TypeEnergie getTypeE() {
        return type;
    }

    public void setType(TypeEnergie type) {
        this.type = type;
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
