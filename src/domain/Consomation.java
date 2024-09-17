package domain;

import java.time.LocalDate;

public abstract class Consomation {
    private int value;
    private LocalDate startDate;
    private LocalDate endDate;
    private User user;
    private int type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Consomation(int value, LocalDate startDate, LocalDate endDate, int type) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }
    public abstract Double calculImpact();

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Consomation{" +
                "value=" + value +
                ", StartDate=" + startDate +
                ", EndDate=" + endDate +
                '}';
    }
}
