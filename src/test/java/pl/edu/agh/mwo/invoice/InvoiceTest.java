package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumberGreaterThan0() {
        int number = invoice.getNumber();
        Assert.assertThat(number, Matchers.greaterThan(0));
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }

    @Test
    public void testTheFirstLineInProductListIsInvoiceNumber() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice1.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice1.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoice1.generateProductList();
        ArrayList<String> productList = invoice1.getProductList();
        int number1 = invoice1.getNumber();
        String invoiceNumber = "Numer faktury: ";
        String firstLineInProductList = productList.get(0);
        Assert.assertEquals(invoiceNumber + String.valueOf(number1), firstLineInProductList);
    }

    @Test
    public void testTheLastLineInProductListIsItemNumber() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice1.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice1.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoice1.generateProductList();
        ArrayList<String> productList = invoice1.getProductList();
        String invoiceItem = "Liczba pozycji: ";
        String lastLineInProductList = productList.get(productList.size() - 1);
        Assert.assertEquals(invoiceItem + String.valueOf(invoice1.getItemCounter()), lastLineInProductList);
    }

    @Test
    public void testTheProductListCounterHasProperValue() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice1.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice1.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoice1.generateProductList();
        Assert.assertEquals(3, invoice1.getItemCounter());
    }

    @Test
    public void testTheProductListHasProperValue() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice1.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice1.generateProductList();
        ArrayList<String> productList = invoice1.getProductList();
        String secondLineInProductList = productList.get(1);
        Assert.assertEquals("Chleb, 2, 5", secondLineInProductList);
    }

    @Test
    public void testTheProductListHasProperDuplicatedValues() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice1.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice1.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoice1.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 125);
        int specyficProductQuantity = 0;
        Map<Product, Integer> product1 = invoice1.getProducts();
        for (Map.Entry<Product, Integer> entry : product1.entrySet()) {
            if (entry.getKey().getName() == "Chleb") {
                specyficProductQuantity = entry.getValue();
            }
        }
        Assert.assertEquals(specyficProductQuantity, 127);
    }

    @Test
    public void testTheExciseProductListHasProperValue() {
        Invoice invoice1 = new Invoice();
        invoice1.addProduct(new BottleOfWine("Champagne", new BigDecimal("100")), 2);
        invoice1.addProduct(new FuelCanister("Diesel", new BigDecimal("8")), 50);
        invoice1.addProduct(new FuelCanister("Petrol", new BigDecimal("7")), 60);
        invoice1.generateProductList();
        ArrayList<String> productList = invoice1.getProductList();
        String secondLineInProductList = productList.get(1);
        Assert.assertEquals("Champagne, 2, 123.00", secondLineInProductList);
    }

}
