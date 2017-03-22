
CREATE FOREIGN PROCEDURE myProcedure (
	IN newParameter_1 string(4000)
) RETURNS
	TABLE (
		newColumn_1 string(4000),
		newColumn_2 string(4000)
) OPTIONS(ANNOTATION 'procedure description', NAMEINSOURCE 'myProcedureSource', "NON-PREPARED" 'TRUE', UPDATECOUNT '1');

