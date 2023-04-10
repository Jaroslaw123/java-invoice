package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Invoice {
    private final LinkedHashMap<Product, Integer> products = new LinkedHashMap<>();
    private static int nextNumber = 0;
    private final int invoiceNumber = ++nextNumber;
    private int itemCounter = 0;

    public Map<Product, Integer> getProducts() {
        return products;
    }

    private final ArrayList<String> productList = new ArrayList<>();


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
        boolean equalProductFlag = false;
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if (Objects.equals(entry.getKey().getName(), product.getName())) {
                entry.setValue(entry.getValue() + quantity);
                equalProductFlag = true;
                break;
            }
        }
        if (!equalProductFlag) {
            products.put(product, quantity);
        }
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
        int setPrecision = 4;
        MathContext precision = new MathContext(setPrecision);
        productList.clear();
        productList.add("Numer faktury: " + String.valueOf(invoiceNumber));
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            String productItem = entry.getKey().getName() + ", " + String.valueOf(entry.getValue())
                    + ", " + String.valueOf(entry.getKey().getPriceWithTax().round(precision));
            productList.add(productItem);
            incrementItemCounter();
        }
        productList.add("Liczba pozycji: " + String.valueOf(getItemCounter()));
        return productList;
    }
}
