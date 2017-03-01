
CREATE FOREIGN TABLE myTable (
	Column1 string(4000),
	CONSTRAINT UniqueConstraint UNIQUE(Column1) OPTIONS(ANNOTATION 'UniqueConstraint description', NAMEINSOURCE 'UniqueConstraintSource')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE')

