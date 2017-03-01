
CREATE FOREIGN TABLE myTable (
	Column1 string(4000),
	Column2 string(4000) NOT NULL,
	Column3 string(4000) NOT NULL,
	CONSTRAINT PrimaryKey PRIMARY KEY(Column3, Column2)OPTIONS(ANNOTATION 'This is PrimaryKey description', NAMEINSOURCE 'PrimaryKeySource')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE')

