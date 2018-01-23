

CREATE FOREIGN TABLE myTable (
	column1 string(4000),
	column2 string(4000),
	CONSTRAINT Index1 INDEX(column1) OPTIONS(ANNOTATION 'Index 1 description', NAMEINSOURCE 'Index1Source'),
	CONSTRAINT Index2 INDEX(column2) OPTIONS(ANNOTATION 'Index 2 description', NAMEINSOURCE 'Index2Source')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE');

