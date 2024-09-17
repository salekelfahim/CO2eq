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
        if (typeE == TypeEnergie.ELECTRICITE) {
            return energie * 0.5;
        } else if (typeE == TypeEnergie.GAZ) {
            return energie * 2.0;
        }
        return 0.0;
    }}
