
SET NAMESPACE 'http://teiid.org/rest' AS REST;

CREATE VIEW myTable (
	tColumn1 biginteger(4000) NOT NULL OPTIONS(NAMEINSOURCE 'tColumn1Source', NATIVE_TYPE 'tColumn1NT', ANNOTATION 'tColumn1 description'),
	tColumn2 string(123) OPTIONS(NAMEINSOURCE 'tColumn2Source', NATIVE_TYPE 'tColumn2NT', ANNOTATION 'tColumn2 description'),
	tColumn3 short OPTIONS(NAMEINSOURCE 'tColumn3Source', NATIVE_TYPE 'tColumn3NT', ANNOTATION 'tColumn3 description')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE') 
AS
	SELECT
		convert(10, biginteger) AS tColumn1, 'test2' AS tColumn2, convert(5, short) AS tColumn3;

CREATE VIRTUAL PROCEDURE myProcedure (IN newParameter_1 string(1234)) RETURNS TABLE (pColumn1 string(1234) OPTIONS(NAMEINSOURCE 'pColumn1Source', NATIVE_TYPE 'pColumn1NT', ANNOTATION 'pColumn1 description'), pColumn2 biginteger(4000) NOT NULL OPTIONS(NAMEINSOURCE 'pColumn2Source', NATIVE_TYPE 'pColumn2NT', ANNOTATION 'pColumn2 description'))
 OPTIONS(NAMEINSOURCE 'myProcedureSource')
	AS
BEGIN
	SELECT * FROM sourceM.sTable WHERE sourceM.sTable.pColumn1 = ViewModelColumns.myProcedure.newParameter_1;
END;

