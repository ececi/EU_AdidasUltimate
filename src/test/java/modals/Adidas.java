package modals;

import enums.EProductCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Adidas {
    static WebDriver driver;
    private static final String addToChartLinkText = "Add to cart";

    private String productName;
    private final String linkText;
    private final EProductCategory productCategory;
    private double amount;

    private Adidas(EProductCategory productCategory, String productName){
        this.productCategory = productCategory;
        this.productName = productName;
        this.linkText = productName;
    }

    public static void setupDriver(WebDriver driver){
        Adidas.driver = driver;
    }

    public String getProductName(){return productName;}

    public EProductCategory getProductCategory(){return productCategory;}

    public String getLinkText(){return linkText;}

    public double getAmount(){return amount;}

    public void updateAmount(){
        amount = Double.parseDouble(driver.findElement(By.cssSelector(".price-container")).getText().split(" ")[0].substring(1));
    }

    public WebElement getMenuCategory(){
        return driver.findElement(By.linkText(getProductCategory().getLinkText()));
    }

    public WebElement getProductLink(){
        return driver.findElement(By.linkText(getLinkText()));
    }

    public WebElement getButtonAddToChart(){
        return driver.findElement(By.linkText(Adidas.addToChartLinkText));
    }

    public static Adidas getInstance(EProductCategory productCategory, String product) {
        return new Adidas(productCategory, product);
    }

    public static class Elements{
        public static WebElement getButtonCart(){
            return driver.findElement(By.linkText("Cart"));
        }

        public static WebElement getButtonPlaceOrder(){
            return driver.findElement(By.xpath("//button[.='Place Order']"));
        }

        /**
         * @param product product to remove
         * @return
         */
        public static WebElement getButtonRemoveProductByProductIndex(Adidas product){
            //<td>Dell i7 8gb</td>          collection
            List<WebElement> tdElementsOfProductsInBox = driver.findElements(By.cssSelector(".success td:nth-of-type(2)"));

            int productIndexToRemove = 0;
            for (int i = 0; i < tdElementsOfProductsInBox.size(); i++) {
                if (tdElementsOfProductsInBox.get(i).getAttribute("innerHTML").equals(product.productName)){
                    productIndexToRemove = i;
                    break;
                }
            }
            return driver.findElement(By.cssSelector("#tbodyid tr:nth-of-type(" + (productIndexToRemove + 1) + ") td:nth-of-type(4) a"));
        }

        public static class PurchaseForm{
            public static WebElement getTextBoxName(){
                return driver.findElement(By.id("name"));
            }
            public static WebElement getTextBoxCountry(){
                return driver.findElement((By.id("country")));
            }
            public static WebElement getTextBoxCity(){
                return driver.findElement(By.id("city"));
            }
            public static WebElement getTextBoxCreditCard(){
                return driver.findElement(By.id("card"));
            }
            public static WebElement getTextBoxMonth(){
                return driver.findElement(By.id("month"));
            }
            public static WebElement getTextBoxYear(){
                return driver.findElement(By.id("year"));
            }
            public static WebElement getButtonPurchase(){
                return driver.findElement(By.xpath("//button[.='Purchase']"));
            }
            public static WebElement getButtonOK(){
                return driver.findElement(By.xpath("//button[.='OK']"));
            }
            private static String[] getPurchaseResultsAsParts(){
                WebElement pPurchaseResult = driver.findElement(By.cssSelector(".lead.text-muted"));
                String[] resultParts = pPurchaseResult.getAttribute("innerHTML").split("<br>");
                return resultParts;
            }
            public static int getResultID(){
                int resultID = Integer.parseInt(getPurchaseResultsAsParts()[0].split(" ")[1]);
                return resultID;
            }
            public static double getResultAmount(){
                double resultAmount = Double.parseDouble(getPurchaseResultsAsParts()[1].split(" ")[1]);
                return resultAmount;
            }
        }


    }
}
