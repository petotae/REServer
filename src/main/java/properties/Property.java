package properties;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

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
        this.propertyId = this.longOrNull(propertyId);
        this.downloadDate = this.stringOrNull(downloadDate);
        this.councilName = this.stringOrNull(councilName);
        this.purchasePrice = this.longOrNull(purchasePrice);
        this.address = this.stringOrNull(address);
        this.postCode = this.longOrNull(postCode);
        this.propertyType = this.stringOrNull(propertyType);
        this.strataLotNumber = this.stringOrNull(strataLotNumber);
        this.propertyName = this.stringOrNull(propertyName);
        this.area = this.doubleOrNull(area);
        this.areaType = this.stringOrNull(areaType);
        this.contractDate = this.stringOrNull(contractDate);
        this.settlementDate = this.stringOrNull(settlementDate);
        this.zoning = this.stringOrNull(zoning);
        this.natureOfProperty = this.stringOrNull(natureOfProperty);
        this.primaryPurpose = this.stringOrNull(primaryPurpose);
        this.legalDescription = this.stringOrNull(legalDescription);
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = this.longOrNull(propertyId);
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = this.stringOrNull(downloadDate);
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(String councilName) {
        this.councilName = this.stringOrNull(councilName);
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = this.longOrNull(purchasePrice);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = this.stringOrNull(address);
    }

    public Long getPostCode() {
        return postCode;
    }

    public void setPostCode(Long postCode) {
        this.postCode = this.longOrNull(postCode);
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = this.stringOrNull(propertyType);
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(String strataLotNumber) {
        this.strataLotNumber = this.stringOrNull(strataLotNumber);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = this.stringOrNull(propertyName);
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = this.doubleOrNull(area);
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = this.stringOrNull(areaType);
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = this.stringOrNull(contractDate);
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = this.stringOrNull(settlementDate);
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = this.stringOrNull(zoning);
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(String natureOfProperty) {
        this.natureOfProperty = this.stringOrNull(natureOfProperty);
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(String primaryPurpose) {
        this.primaryPurpose = this.stringOrNull(primaryPurpose);
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    public void setLegalDescription(String legalDescription) {
        this.legalDescription = this.stringOrNull(legalDescription);
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

    private String stringOrNull(String s) {
        return s == null ? "null" : s;
    }

    private Long longOrNull(Long l) {
        return l == null ? 0 : l;
    }

    private Double doubleOrNull(Double d) {
        return d == null ? 0 : d;
    }
}
