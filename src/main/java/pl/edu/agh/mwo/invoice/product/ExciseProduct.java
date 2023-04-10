package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class ExciseProduct extends Product {
    private static final BigDecimal constTax = new BigDecimal("5.56");
    private static final LocalDate motherInLawDate = LocalDate.of(2023, 03, 05);
    private final LocalDate todayDate;

    protected ExciseProduct(String name, BigDecimal price, BigDecimal tax, LocalDate todayDate)
            throws IllegalArgumentException {
        super(name, price, tax);
        this.todayDate = todayDate;
    }

    @Override
    public BigDecimal getPriceWithTax() {
        if (checkIfMotherInLawDayIsToday()) {
            return super.getPriceWithTax();
        } else {
            return super.getPriceWithTax().add(constTax);
        }

    }

    private boolean checkIfMotherInLawDayIsToday() {
        LocalDate todayDate = this.todayDate;
        int todayDay = todayDate.getDayOfMonth();
        int todayMonth = todayDate.getMonthValue();
        int motherInLawDay = motherInLawDate.getDayOfMonth();
        int motherInLawMonth = motherInLawDate.getMonthValue();
        return todayDay == motherInLawDay && todayMonth == motherInLawMonth;
    }
}
