package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    Map<Product, Integer> productIntegerMap = new HashMap<Product, Integer>();
    public void addProduct(Product product) {
        if (product == null){
            throw new IllegalArgumentException();
        }
        productIntegerMap.put(product,1);
    }
    public void addProduct(Product product, Integer quantity) {
        if(quantity.compareTo(0) <= 0){
            throw new IllegalArgumentException();
        }
        productIntegerMap.put(product,quantity);
    }
    public BigDecimal getSubtotal() {
            BigDecimal subtotal = BigDecimal.ZERO;

            for(Map.Entry<Product, Integer> entry : productIntegerMap.entrySet()){
                subtotal = subtotal.add(entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }

            return subtotal;
    }
    public BigDecimal getTax() {
        BigDecimal fullTax = BigDecimal.ZERO;

        for(Map.Entry<Product, Integer> entry : productIntegerMap.entrySet()){
            fullTax = fullTax.add(entry.getKey().getTaxPercent().multiply(entry.getKey().getPrice()));
        }

        return fullTax;
    }
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for(Map.Entry<Product, Integer> entry : productIntegerMap.entrySet()){
            total = total.add(entry.getKey().getPriceWithTax().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return total;
    }
}
