package enums;

public enum EProductCategory {

    category_Laptop("Laptops"),
    category_Phones("Phones"),
    category_Monitors("Monitors");

    private String linkText;
    EProductCategory(String linkText){
        this.linkText = linkText;
    }

    public String getLinkText(){return linkText;}
}
