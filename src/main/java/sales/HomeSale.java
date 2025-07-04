package sales;

public class HomeSale {

    private long propertyId;
    private String downloadDate;
    private String councilName;
    private long purchasePrice;
    private String address;
    private long postCode;
    private String propertyType;
    private String strataLotNumber;
    private String propertyName;
    private double area;
    private String areaType;
    private String contractDate;
    private String settlementDate;
    private String zoning;
    private String natureOfProperty;
    private String primaryPurpose;
    private String legalDescription;

    // Full constructor
    public HomeSale(final long propertyId, final String downloadDate, final String councilName, final long purchasePrice, final String address,
                    final long postCode, final String propertyType, final String strataLotNumber, final String propertyName, final double area,
                    final String areaType, final String contractDate, final String settlementDate, final String zoning,
                    final String natureOfProperty, final String primaryPurpose, final String legalDescription) {
        this.propertyId = propertyId;
        this.downloadDate = downloadDate;
        this.councilName = councilName;
        this.purchasePrice = purchasePrice;
        this.address = address;
        this.postCode = postCode;
        this.propertyType = propertyType;
        this.strataLotNumber = strataLotNumber;
        this.propertyName = propertyName;
        this.area = area;
        this.areaType = areaType;
        this.contractDate = contractDate;
        this.settlementDate = settlementDate;
        this.zoning = zoning;
        this.natureOfProperty = natureOfProperty;
        this.primaryPurpose = primaryPurpose;
        this.legalDescription = legalDescription;
    }

    // No-arg constructor for JSON
    public HomeSale() {}

    // Getters and setters

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final long propertyId) {
        this.propertyId = propertyId;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(final String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(final String councilName) {
        this.councilName = councilName;
    }

    public long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(final long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public long getPostCode() {
        return postCode;
    }

    public void setPostCode(final long postCode) {
        this.postCode = postCode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(final String strataLotNumber) {
        this.strataLotNumber = strataLotNumber;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public double getArea() {
        return area;
    }

    public void setArea(final double area) {
        this.area = area;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(final String areaType) {
        this.areaType = areaType;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(final String contractDate) {
        this.contractDate = contractDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(final String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(final String zoning) {
        this.zoning = zoning;
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(final String natureOfProperty) {
        this.natureOfProperty = natureOfProperty;
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(final String primaryPurpose) {
        this.primaryPurpose = primaryPurpose;
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    public void setLegalDescription(final String legalDescription) {
        this.legalDescription = legalDescription;
    }
}
