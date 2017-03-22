

CREATE FOREIGN TABLE sTable (
	pColumn1 string(1234),
	pColumn2 biginteger(4000) NOT NULL
) OPTIONS(NAMEINSOURCE 'sTableSource', UPDATABLE 'TRUE');

