
CREATE VIEW myTable (
	Column1 bigdecimal(1) NOT NULL,
	newColumn_2 string(4000),
	newColumn_3 string(4000),
	CONSTRAINT PrimaryKey PRIMARY KEY(Column1) OPTIONS(ANNOTATION 'PrimaryKey description', NAMEINSOURCE 'PrimaryKeySource')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE') 
AS
	SELECT
		convert(10, bigdecimal) AS Column1, 'test2' AS newColumn_2, 'test3' AS newColumn_3;

