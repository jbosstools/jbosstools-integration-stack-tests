
SET NAMESPACE 'http://teiid.org/rest' AS REST;

CREATE GLOBAL TEMPORARY TABLE tempTable (
	id string(30),
	QUANTITIY bigdecimal,
	isNew boolean
)
CREATE VIRTUAL PROCEDURE getProduct (id string(4000)) RETURNS TABLE (NAME string(60))
	AS
BEGIN
 SELECT sourceModel.PRODUCTDATA.NAME FROM sourceModel.PRODUCTDATA WHERE viewModel.getProduct.id = sourceModel.PRODUCTDATA.INSTR_ID;
END;

CREATE VIRTUAL PROCEDURE selfProc (stringIN string(4000)) RETURNS TABLE (result string(30))
	AS
BEGIN
 SELECT viewModel.selfProc.stringIN AS result;
END;

