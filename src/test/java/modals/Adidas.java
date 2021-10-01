package modals;

import enums.EProductCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Adidas {
    static WebDriver driver;
    private static final String addToChartLinkText = "Add to cart";

    private String product;
    private final String linkText;
    private final EProductCategory productCategory;
    private double amount;

    private Adidas(EProductCategory productCategory, String product){
        this.productCategory = productCategory;
        this.product = product;
        this.linkText = product;
    }

    public static void setupDriver(WebDriver driver){
        Adidas.driver = driver;
    }

    public EProductCategory getProductCategory(){return productCategory;}

    public String getLinkText(){return linkText;}

    public double getAmount(){return amount;}

    public void setAmount(double amount){this.amount = amount;}

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
         * @param productIndex zero based index
         * @return
         */
        public static WebElement getButtonRemoveProductByProductIndex(int productIndex){
            return driver.findElement(By.cssSelector("#tbodyid tr:nth-of-type(" + (productIndex + 1) + ") td:nth-of-type(4) a"));
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
