
CREATE GLOBAL TEMPORARY TABLE viewTable (
	id string(30),
	QUANTITY bigdecimal,
	isNew boolean
) OPTIONS(UPDATABLE 'TRUE')
