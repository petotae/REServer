package sales;

/**
 * HomeSale represents a sale of a home property.
 * It contains various attributes related to the property and the sale.
 * This class is used to store and retrieve home sale data.
 */
public class HomeSale {

    /**
     * Unique identifier for the property.
     */
    private long propertyId;
    /**
     * Date when the property data was downloaded.
     */
    private String downloadDate;
    /**
     * Name of the council where the property is located.
     */
    private String councilName;
    /**
     * Purchase price of the property.
     */
    private long purchasePrice;
    /**
     * Address of the property.
     */
    private String address;
    /**
     * Postcode of the property.
     */
    private long postCode;
    /**
     * Type of the property.
     */
    private String propertyType;
    /**
     * Strata lot number if applicable.
     */
    private String strataLotNumber;
    /**
     * Name of the property, if applicable.
     */
    private String propertyName;
    /**
     * Area of the property.
     */
    private double area;
    /**
     * Type of area measurement.
     */
    private String areaType;
    /**
     * Date when the contract for the property was signed.
     */
    private String contractDate;
    /**
     * Date when the property settlement occurred.
     */
    private String settlementDate;
    /**
     * Zoning classification of the property.
     */
    private String zoning;
    /**
     * Nature of the property).
     */
    private String natureOfProperty;
    /**
     * Primary purpose of the property.
     */
    private String primaryPurpose;
    /**
     * Legal description of the property.
     */
    private String legalDescription;

    /**
     * Constructor for HomeSale
     * @param propertyId
     * @param downloadDate
     * @param councilName
     * @param purchasePrice
     * @param address
     * @param postCode
     * @param propertyType
     * @param strataLotNumber
     * @param propertyName
     * @param area
     * @param areaType
     * @param contractDate
     * @param settlementDate
     * @param zoning
     * @param natureOfProperty
     * @param primaryPurpose
     * @param legalDescription
     */
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

    /**
     * Default constructor for HomeSale.
     * This is required for frameworks that require a no-argument constructor.
     */
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
