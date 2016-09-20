BEGIN
	DECLARE string VARIABLES.IN_INSTR_ID;
	VARIABLES.IN_INSTR_ID = xpathValue(ProductsWs.ProductInfo.deleteProductInfo.deleteProductInfo_Input, '/*:IdInput/*:INSTR_ID');
	DECLARE integer VARIABLES.delete_count = 0;
	DELETE FROM ProductsView.ProductInfo WHERE ProductsView.ProductInfo.INSTR_ID = VARIABLES.IN_INSTR_ID;
	VARIABLES.delete_count = VARIABLES.ROWCOUNT;
	IF(VARIABLES.delete_count = 1)
	BEGIN
		SELECT * FROM ProductsWsResponses.OkResultDocument;
	END
	ELSE
	BEGIN
		SELECT * FROM ProductsWsResponses.FailedResultDocument;
	END
END