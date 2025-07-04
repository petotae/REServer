package properties;

public enum PropertyDataField {
    PROPERTY_ID(1),
    DOWNLOAD_DATE(2),
    COUNCIL_NAME(3),
    PURCHASE_PRICE(4),
    ADDRESS(5),
    POST_CODE(6),
    PROPERTY_TYPE(7),
    STRATA_LOT_NUMBER(8),
    PROPERTY_NAME(9),
    AREA(10),
    AREA_TYPE(11),
    CONTRACT_DATE(12),
    SETTLEMENT_DATE(13),
    ZONING(14),
    NATURE_OF_PROPERTY(15),
    PRIMARY_PURPOSE(16),
    LEGAL_DESCRIPTION(17);

    private final int index;

    PropertyDataField(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}