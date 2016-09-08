/* First comment in the criteria builder table */
SELECT
		PartsSupplier.SUPPLIER.SUPPLIER_ID, PartsSupplier.SUPPLIER.SUPPLIER_NAME, PartsSupplier.SUPPLIER.SUPPLIER_STATUS, PartsSupplier.SUPPLIER.SUPPLIER_CITY, PartsSupplier.SUPPLIER.SUPPLIER_STATE, PartsSupplier.PARTS.PART_ID, PartsSupplier.PARTS.PART_NAME, PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_WEIGHT, PartsSupplier.SUPPLIER_PARTS.SUPPLIER_ID AS SUPPLIER_ID_1, PartsSupplier.SUPPLIER_PARTS.PART_ID AS PART_ID_1, PartsSupplier.SUPPLIER_PARTS.QUANTITY, PartsSupplier.SUPPLIER_PARTS.SHIPPER_ID
	FROM
/* Second comment in the criteria builder table */
		PartsSupplier.SUPPLIER, PartsSupplier.PARTS, PartsSupplier.SUPPLIER_PARTS
