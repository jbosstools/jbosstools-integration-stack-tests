
SET NAMESPACE 'http://teiid.org/rest' AS REST;

CREATE VIRTUAL PROCEDURE myProcedure (
	IN Parameter1 string(4000)
) RETURNS
	TABLE (
		c1 string(4000)
) OPTIONS(ANNOTATION 'Procedure description', NAMEINSOURCE 'myProcedureSource', UPDATECOUNT '1')
	AS
BEGIN
	SELECT * FROM sourceM.myTable WHERE sourceM.myTable.c1 = ViewProcedureSettings.myProcedure.Parameter1;
END;
