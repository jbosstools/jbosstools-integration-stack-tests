

CREATE VIEW viewTable (
	col1 string(4000),
	CONSTRAINT viewAP ACCESSPATTERN(col1) OPTIONS(ANNOTATION 'view AP description', NAMEINSOURCE 'viewAPsource')
) OPTIONS(NAMEINSOURCE 'viewTableSource', UPDATABLE 'TRUE') 
AS
	SELECT
		'test' AS col1;

