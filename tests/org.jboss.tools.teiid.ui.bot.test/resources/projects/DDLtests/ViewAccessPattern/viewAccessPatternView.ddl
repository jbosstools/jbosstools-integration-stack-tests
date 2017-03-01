
CREATE VIEW viewTable (
	SUPPLIER_ID string(10) NOT NULL,
	SUPPLIER_NAME string(30),
	SUPPLIER_STATUS bigdecimal OPTIONS(FIXED_LENGTH 'TRUE'),
	SUPPLIER_CITY string(30),
	SUPPLIER_STATE string(2),
	CONSTRAINT NAME ACCESSPATTERN(SUPPLIER_NAME)
) OPTIONS(UPDATABLE 'TRUE') 
AS
	SELECT * FROM viewAccessPatternSource.SUPPLIER;

