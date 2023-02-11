package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Collection<Product> products;

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
        Map<Product, Integer> productIntegerMap = new HashMap<Product, Integer>();
        productIntegerMap.put(product,quantity);
    }
    public BigDecimal getSubtotal() {

        return BigDecimal.ZERO;
    }

    public BigDecimal getTax() {
        return null;
    }

    public BigDecimal getTotal() {

        return BigDecimal.ZERO;
    }
}
