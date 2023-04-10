package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

public class ExciseProduct extends Product {
    private static final BigDecimal constTax = new BigDecimal(5.56);

    protected ExciseProduct(String name, BigDecimal price, BigDecimal tax) throws IllegalArgumentException {
        super(name, price, tax);
    }

    @Override
    public BigDecimal getPrice() {
        if (checkIfMotherInLawDayIsToday()) {
            return super.getPrice().add(constTax);
        } else {
            return super.getPrice();
        }

    }

    private static boolean checkIfMotherInLawDayIsToday() {
        LocalDate todayDate = LocalDate.now();
        int todayDay = todayDate.getDayOfMonth();
        int todayMonth = todayDate.getMonthValue();

        LocalDate motherInLawDate = LocalDate.of(2023, 03, 05);
        int motherinLawDay = motherInLawDate.getDayOfMonth();
        int motherinLawMonth = motherInLawDate.getMonthValue();
        if (todayDay == motherinLawDay && todayMonth == motherinLawMonth) {
            return true;
        } else {
            return false;
        }
    }
}
