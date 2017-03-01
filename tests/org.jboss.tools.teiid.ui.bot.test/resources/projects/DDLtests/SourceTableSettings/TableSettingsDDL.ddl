
CREATE FOREIGN TABLE myTable (
	col1 string(4000)
) OPTIONS(NAMEINSOURCE 'myTableSource', MATERIALIZED 'TRUE', UPDATABLE 'TRUE', CARDINALITY '120', MATERIALIZED_TABLE 'TableSettingsSourceModel.helpTable', ANNOTATION 'This is Table description')

CREATE FOREIGN TABLE helpTable (
	newColumn_1 string(4000)
) OPTIONS(NAMEINSOURCE 'source', UPDATABLE 'TRUE')

