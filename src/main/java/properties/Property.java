package properties;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * Represents a property with various attributes.
 * This class provides getters and setters for each attribute,
 * and allows dynamic access to properties by name.
 */
public class Property {
    /**
     * Unique identifier for the property.
     */
    private Long propertyId;
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
    private Long purchasePrice;
    /**
     * Address of the property.
     */
    private String address;
    /**
     * Postcode of the property.
     */
    private Long postCode;
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
    private Double area;
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
     * Nature of the property.
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
     * Default constructor for Property.
     * Initializes a new instance of the Property class.
     */
    public Property() {
    }

    public Property(
            final Long propertyId,
            final String downloadDate,
            final String councilName,
            final Long purchasePrice,
            final String address,
            final Long postCode,
            final String propertyType,
            final String strataLotNumber,
            final String propertyName,
            final Double area,
            final String areaType,
            final String contractDate,
            final String settlementDate,
            final String zoning,
            final String natureOfProperty,
            final String primaryPurpose,
            final String legalDescription) {
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

    public void setPropertyId(final Long propertyId) {
        this.propertyId = this.longOrNull(propertyId);
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(final String downloadDate) {
        this.downloadDate = this.stringOrNull(downloadDate);
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(final String councilName) {
        this.councilName = this.stringOrNull(councilName);
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(final Long purchasePrice) {
        this.purchasePrice = this.longOrNull(purchasePrice);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = this.stringOrNull(address);
    }

    public Long getPostCode() {
        return postCode;
    }

    public void setPostCode(final Long postCode) {
        this.postCode = this.longOrNull(postCode);
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = this.stringOrNull(propertyType);
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(final String strataLotNumber) {
        this.strataLotNumber = this.stringOrNull(strataLotNumber);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = this.stringOrNull(propertyName);
    }

    public Double getArea() {
        return area;
    }

    public void setArea(final Double area) {
        this.area = this.doubleOrNull(area);
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(final String areaType) {
        this.areaType = this.stringOrNull(areaType);
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(final String contractDate) {
        this.contractDate = this.stringOrNull(contractDate);
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(final String settlementDate) {
        this.settlementDate = this.stringOrNull(settlementDate);
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(final String zoning) {
        this.zoning = this.stringOrNull(zoning);
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(final String natureOfProperty) {
        this.natureOfProperty = this.stringOrNull(natureOfProperty);
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(final String primaryPurpose) {
        this.primaryPurpose = this.stringOrNull(primaryPurpose);
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    /**
     * Sets the legal description of the property.
     *
     * @param legalDescription the legal description to set
     */
    public void setLegalDescription(final String legalDescription) {
        this.legalDescription = this.stringOrNull(legalDescription);
    }

    /**
     * Returns a string representation of the property.
     *
     * @return a string containing all property details
     */
    public Object get(final String property) {
        try {
            final PropertyDescriptor propDesc = new PropertyDescriptor(property, this.getClass());
            return propDesc.getReadMethod().invoke(this);
        } catch (IntrospectionException | ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Unable to get property '" + property + "'", e);
        }
    }

    /**
     * Sets a property value dynamically by its name.
     *
     * @param property the name of the property to set
     * @param value    the value to set for the property
     */
    public void set(final String property, final Object value) {
        try {
            final PropertyDescriptor propDesc = new PropertyDescriptor(property, this.getClass());
            propDesc.getWriteMethod().invoke(this, value);
        } catch (IntrospectionException | ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Unable to set property '" + property + "' to " + value, e);
        }
    }

    private String stringOrNull(final String str) {
        return str == null ? "null" : str;
    }

    private Long longOrNull(final Long lon) {
        return lon == null ? 0 : lon;
    }

    private Double doubleOrNull(final Double dou) {
        return dou == null ? 0 : dou;
    }
}
