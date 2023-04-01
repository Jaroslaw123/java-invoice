package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<>();
    private static int nextNumber = 0;
    private final int invoiceNumber = ++nextNumber;
    private ArrayList<String> productList = new ArrayList<String>();
    private int itemCounter = 0;

    public int getItemCounter() {
        return this.itemCounter;
    }

    public int getNumber() {
        return this.invoiceNumber;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public void incrementItemCounter() {
        itemCounter++;
    }

    public ArrayList<String> getProductList() {
        return productList;
    }

    public ArrayList<String> generateProductList() {
        productList.clear();
        productList.add("Numer faktury: " + String.valueOf(invoiceNumber));
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            String productItem = entry.getKey().getName() + ", " + String.valueOf(entry.getValue()) + ", " + String.valueOf(entry.getKey().getPrice());
            productList.add(productItem);
            incrementItemCounter();
        }
        productList.add("Liczba pozycji: " + String.valueOf(getItemCounter()));
        return productList;
    }
}
