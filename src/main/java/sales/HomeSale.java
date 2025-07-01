package sales;

// Simple class to provide test data in SalesDAO

public class HomeSale {
    public String saleID;
    public String postcode;
    public String salePrice;

    public HomeSale(String saleID, String postcode, String salePrice) {
        this.saleID = saleID;
        this.postcode = postcode;
        this.salePrice = salePrice;
    }

    // needed for JSON conversion
    public HomeSale() {
    }

    public String getID() {
        return this.saleID;
    }

    public void setID(String id) {
        this.saleID = id;
    }

    public String getPostCode() {
        return this.postcode;
    }

    public void setPostCode(String pc) {
        this.postcode = pc;
    }

    public String getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(String sp) {
        this.salePrice = sp;
    }

}
