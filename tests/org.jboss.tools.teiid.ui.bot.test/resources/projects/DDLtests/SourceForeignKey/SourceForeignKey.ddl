
CREATE FOREIGN TABLE helpTable (
	Column1 string(4000) NOT NULL,
	Column2 string(4000),
	CONSTRAINT PrimaryKey PRIMARY KEY(Column1) OPTIONS(NAMEINSOURCE 'PrimaryKeySource'),
	CONSTRAINT UniqueConstraint UNIQUE(Column2) OPTIONS(NAMEINSOURCE 'UniqueConstraintSource')
) OPTIONS(NAMEINSOURCE 'helpTableSource', UPDATABLE 'TRUE');

CREATE FOREIGN TABLE myTable (
	fk_Column1 string(4000),
	fk_Column2 string(4000),
	CONSTRAINT ForeignKey1 FOREIGN KEY(fk_Column1) REFERENCES helpTable(Column1) OPTIONS(NAMEINSOURCE 'ForeignKey1Source'),
	CONSTRAINT ForeignKey2 FOREIGN KEY(fk_Column2) REFERENCES helpTable(Column2) OPTIONS(NAMEINSOURCE 'ForeignKey2Source')
) OPTIONS(NAMEINSOURCE 'myTableSource', UPDATABLE 'TRUE');

