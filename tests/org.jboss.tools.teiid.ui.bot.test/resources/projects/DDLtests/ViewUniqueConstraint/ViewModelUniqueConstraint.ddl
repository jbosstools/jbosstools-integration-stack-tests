
CREATE VIEW myTable (
	Column1 string(4000),
	Column2 string(4000),
	CONSTRAINT UniqueConstraint UNIQUE(Column2) OPTIONS(ANNOTATION 'UniqueConstraint description', NAMEINSOURCE 'UniqueConstraintSource')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE') 
AS
	SELECT
		'test' AS Column1, 'test2' AS Column2;

