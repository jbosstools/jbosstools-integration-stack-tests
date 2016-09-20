BEGIN
	DECLARE string VARIABLES.IN_PRIBUSINESS;
	VARIABLES.IN_PRIBUSINESS = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:PRIBUSINESS');
	DECLARE string VARIABLES.IN_ISAMEXINT;
	VARIABLES.IN_ISAMEXINT = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:ISAMEXINT');
	DECLARE string VARIABLES.IN_ISNAS100;
	VARIABLES.IN_ISNAS100 = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:ISNAS100');
	DECLARE string VARIABLES.IN_ISSP500;
	VARIABLES.IN_ISSP500 = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:ISSP500');
	DECLARE string VARIABLES.IN_ISDJI;
	VARIABLES.IN_ISDJI = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:ISDJI');
	DECLARE string VARIABLES.IN_EXCHANGE;
	VARIABLES.IN_EXCHANGE = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:EXCHANGE');
	DECLARE string VARIABLES.IN_ISSUER;
	VARIABLES.IN_ISSUER = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:ISSUER');
	DECLARE string VARIABLES.IN_TYPE;
	VARIABLES.IN_TYPE = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:TYPE');
	DECLARE string VARIABLES.IN_NAME;
	VARIABLES.IN_NAME = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:NAME');
	DECLARE string VARIABLES.IN_CUSIP;
	VARIABLES.IN_CUSIP = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:CUSIP');
	DECLARE string VARIABLES.IN_SYMBOL;
	VARIABLES.IN_SYMBOL = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:SYMBOL');
	DECLARE string VARIABLES.IN_SYMBOL_TYPE;
	VARIABLES.IN_SYMBOL_TYPE = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:SYMBOL_TYPE');
	DECLARE string VARIABLES.IN_INSTR_ID;
	VARIABLES.IN_INSTR_ID = xpathValue(ProductsWs.ProductInfo.insertProductInfo.insertProductInfo_Input, '/*:ProductInput/*:ProductInput_Instance/*:INSTR_ID');
	DECLARE integer VARIABLES.update_count = 0;  
	BEGIN  
        INSERT INTO ProductsView.ProductInfo (INSTR_ID, SYMBOL_TYPE, SYMBOL, CUSIP, NAME, TYPE, ISSUER, EXCHANGE, ISDJI, ISSP500, ISNAS100, ISAMEXINT, PRIBUSINESS) VALUES (VARIABLES.IN_INSTR_ID, convert(VARIABLES.IN_SYMBOL_TYPE, integer), VARIABLES.IN_SYMBOL, VARIABLES.IN_CUSIP, VARIABLES.IN_NAME, VARIABLES.IN_TYPE, VARIABLES.IN_ISSUER, VARIABLES.IN_EXCHANGE, convert(VARIABLES.IN_ISDJI, boolean), convert(VARIABLES.IN_ISSP500, boolean), convert(VARIABLES.IN_ISNAS100, boolean), convert(VARIABLES.IN_ISAMEXINT, boolean), VARIABLES.IN_PRIBUSINESS);  
        VARIABLES.update_count = VARIABLES.ROWCOUNT;  
        IF(VARIABLES.update_count = 1)  
        BEGIN  
            SELECT * FROM ProductsWsResponses.OkResultDocument;  
        END  
        ELSE  
        BEGIN  
            SELECT * FROM ProductsWsResponses.FailedResultDocument;  
        END  
    EXCEPTION e  
        BEGIN  
            RAISE SQLWARNING e.EXCEPTION;  
            SELECT * FROM ProductsWsResponses.FailedResultDocument;  
        END  
    END
END