

CREATE VIEW myTable (
	column1 string(4000),
	column2 string(4000),
	CONSTRAINT Index1 INDEX(column1) OPTIONS(ANNOTATION 'Index 1 description', NAMEINSOURCE 'Index1Source'),
	CONSTRAINT Index2 INDEX(column2) OPTIONS(ANNOTATION 'Index 2 description', NAMEINSOURCE 'Index2Source')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE') 
AS
	SELECT
		'test' AS column1, 'test2' AS column2;

