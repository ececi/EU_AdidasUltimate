package modals;

import enums.EProductCategory;
import org.openqa.selenium.By;

public class Adidas {

    public static final String addToChartLinkText = "Add to cart";
    public static final By byCart = new By.ByLinkText("Cart");

    private String product;
    private final String linkText;
    private final EProductCategory productCategory;
    private double amount;

    private Adidas(EProductCategory productCategory, String product){
        this.productCategory = productCategory;
        this.product = product;
        this.linkText = product;
    }

    public EProductCategory getProductCategory(){return productCategory;}

    public String getLinkText(){return linkText;}

    public double getAmount(){return amount;}

    public void setAmount(double amount){this.amount = amount;}

    public static Adidas getInstance(EProductCategory productCategory, String product) {
        return new Adidas(productCategory, product);
    }


}
