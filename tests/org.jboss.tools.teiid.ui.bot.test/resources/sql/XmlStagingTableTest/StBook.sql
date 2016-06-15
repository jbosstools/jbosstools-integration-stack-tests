SELECT SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookISBN AS isbn, SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookTITLE AS title 
FROM SchemaModel.StagingDocument.MappingClasses.ST_ResultSet 
WHERE INPUTS.publisherId = SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PUBLISHER_ID