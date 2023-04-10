package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.util.Date;

public class FuelCanister extends ExciseProduct{
    public FuelCanister(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"));
    }
}
