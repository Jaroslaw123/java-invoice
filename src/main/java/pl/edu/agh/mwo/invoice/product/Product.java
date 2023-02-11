package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        try {
            this.name = name;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        this.price = price;
        this.taxPercent = tax;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public BigDecimal getTaxPercent() {
        return taxPercent;
    }
    public BigDecimal getPriceWithTax() {
        return price.add(price.multiply(taxPercent));
    }
}
