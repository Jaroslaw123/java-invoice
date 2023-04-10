package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BottleOfWine extends ExciseProduct {
    public BottleOfWine(String name, BigDecimal price, LocalDate todayDate) {
        super(name, price, new BigDecimal("0.23"), todayDate);
    }

    public BottleOfWine(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"), LocalDate.now());
    }
}
