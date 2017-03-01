
CREATE FOREIGN TABLE myTable (
	tColumn1 bigdecimal(1) NOT NULL OPTIONS(NAMEINSOURCE 'tColumn1Source', NATIVE_TYPE 'tColumn1NT', ANNOTATION 'tColumn1 description'),
	tColumn2 float OPTIONS(NAMEINSOURCE 'tColumn2Source', NATIVE_TYPE 'tColumn2NT', ANNOTATION 'tColumn2 description'),
	tColumn3 string(123) OPTIONS(NAMEINSOURCE 'tColumn3Source', NATIVE_TYPE 'tColumn3NT', ANNOTATION 'tColumn3 description')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE')

CREATE FOREIGN PROCEDURE myProcedure (IN newParameter_1 string(4000)) RETURNS TABLE (pColumn1 biginteger(4000) NOT NULL OPTIONS(NAMEINSOURCE 'pColumn1Source', NATIVE_TYPE 'pColumn1NT', ANNOTATION 'pColumn1 description'), pColumn2 string(456) OPTIONS(NAMEINSOURCE 'pColumn2Source', NATIVE_TYPE 'pColumn2NT', ANNOTATION 'pColumn2 description'))
 OPTIONS(NAMEINSOURCE 'myProcedureSource')

