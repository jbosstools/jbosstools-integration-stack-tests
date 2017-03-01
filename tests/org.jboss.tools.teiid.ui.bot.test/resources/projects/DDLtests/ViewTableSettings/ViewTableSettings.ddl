
CREATE VIEW myTable (
	Column1 string(4000)
) OPTIONS(NAMEINSOURCE 'myTableSource', MATERIALIZED 'TRUE', UPDATABLE 'TRUE', CARDINALITY '120', MATERIALIZED_TABLE 'ViewTableSettings.helpTable', ANNOTATION 'Table description') 
AS
	SELECT
		'test' AS Column1;

CREATE VIEW helpTable (
	newColumn_1 string(4000)
) OPTIONS(NAMEINSOURCE 'HTsource', UPDATABLE 'TRUE') 
AS
	SELECT
		'test' AS newColumn_1;

