package properties;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Objects;

public class Property {
    private Long propertyId;
    private String downloadDate;
    private String councilName;
    private Long purchasePrice;
    private String address;
    private Long postCode;
    private String propertyType;
    private String strataLotNumber;
    private String propertyName;
    private Double area;
    private String areaType;
    private String contractDate;
    private String settlementDate;
    private String zoning;
    private String natureOfProperty;
    private String primaryPurpose;
    private String legalDescription;

    public Property() {
    }

    public Property(
            Long propertyId,
            String downloadDate,
            String councilName,
            Long purchasePrice,
            String address,
            Long postCode,
            String propertyType,
            String strataLotNumber,
            String propertyName,
            Double area,
            String areaType,
            String contractDate,
            String settlementDate,
            String zoning,
            String natureOfProperty,
            String primaryPurpose,
            String legalDescription) {
        this.propertyId = Objects.requireNonNull(propertyId, "propertyId is required");
        this.downloadDate = Objects.requireNonNull(downloadDate, "downloadDate is required");
        this.councilName = Objects.requireNonNull(councilName, "councilName is required");
        this.purchasePrice = Objects.requireNonNull(purchasePrice, "purchasePrice is required");
        this.address = Objects.requireNonNull(address, "address is required");
        this.postCode = Objects.requireNonNull(postCode, "postCode is required");
        this.propertyType = Objects.requireNonNull(propertyType, "propertyType is required");
        this.strataLotNumber = Objects.requireNonNull(strataLotNumber, "strataLotNumber is required");
        this.propertyName = Objects.requireNonNull(propertyName, "propertyName is required");
        this.area = Objects.requireNonNull(area, "area is required");
        this.areaType = Objects.requireNonNull(areaType, "areaType is required");
        this.contractDate = Objects.requireNonNull(contractDate, "contractDate is required");
        this.settlementDate = Objects.requireNonNull(settlementDate, "settlementDate is required");
        this.zoning = Objects.requireNonNull(zoning, "zoning is required");
        this.natureOfProperty = Objects.requireNonNull(natureOfProperty, "natureOfProperty is required");
        this.primaryPurpose = Objects.requireNonNull(primaryPurpose, "primaryPurpose is required");
        this.legalDescription = Objects.requireNonNull(legalDescription, "legalDescription is required");
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = Objects.requireNonNull(propertyId, "propertyId is required");
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = Objects.requireNonNull(downloadDate, "downloadDate is required");
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(String councilName) {
        this.councilName = Objects.requireNonNull(councilName, "councilName is required");
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = Objects.requireNonNull(purchasePrice, "purchasePrice is required");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Objects.requireNonNull(address, "address is required");
    }

    public Long getPostCode() {
        return postCode;
    }

    public void setPostCode(Long postCode) {
        this.postCode = Objects.requireNonNull(postCode, "postCode is required");
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = Objects.requireNonNull(propertyType, "propertyType is required");
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(String strataLotNumber) {
        this.strataLotNumber = Objects.requireNonNull(strataLotNumber, "strataLotNumber is required");
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = Objects.requireNonNull(propertyName, "propertyName is required");
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = Objects.requireNonNull(area, "area is required");
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = Objects.requireNonNull(areaType, "areaType is required");
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = Objects.requireNonNull(contractDate, "contractDate is required");
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = Objects.requireNonNull(settlementDate, "settlementDate is required");
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = Objects.requireNonNull(zoning, "zoning is required");
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(String natureOfProperty) {
        this.natureOfProperty = Objects.requireNonNull(natureOfProperty, "natureOfProperty is required");
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(String primaryPurpose) {
        this.primaryPurpose = Objects.requireNonNull(primaryPurpose, "primaryPurpose is required");
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    public void setLegalDescription(String legalDescription) {
        this.legalDescription = Objects.requireNonNull(legalDescription, "legalDescription is required");
    }

    public Object get(String property) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(property, this.getClass());
            return pd.getReadMethod().invoke(this);
        } catch (IntrospectionException | ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Unable to get property '" + property + "'", e);
        }
    }

    public void set(String property, Object value) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(property, this.getClass());
            pd.getWriteMethod().invoke(this, value);
        } catch (IntrospectionException | ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Unable to set property '" + property + "' to " + value, e);
        }
    }
}
