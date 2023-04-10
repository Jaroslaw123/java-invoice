package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuelCanister extends ExciseProduct {
    public FuelCanister(String name, BigDecimal price, LocalDate todayDate) {
        super(name, price, new BigDecimal("0.23"), todayDate);
    }

    public FuelCanister(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"), LocalDate.now());
    }
}
