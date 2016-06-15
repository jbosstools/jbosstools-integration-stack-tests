DECLARE integer VARIABLES.update_count = 0;  
	BEGIN  
        INSERT INTO RelationalModel.ProductInfo (INSTR_ID, SYMBOL_TYPE, SYMBOL, CUSIP, NAME, TYPE, ISSUER, EXCHANGE, ISDJI, ISSP500, ISNAS100, ISAMEXINT, PRIBUSINESS) VALUES (VARIABLES.IN_INSTR_ID, convert(VARIABLES.IN_SYMBOL_TYPE, integer), VARIABLES.IN_SYMBOL, VARIABLES.IN_CUSIP, VARIABLES.IN_NAME, VARIABLES.IN_TYPE, VARIABLES.IN_ISSUER, VARIABLES.IN_EXCHANGE, convert(VARIABLES.IN_ISDJI, boolean), convert(VARIABLES.IN_ISSP500, boolean), convert(VARIABLES.IN_ISNAS100, boolean), convert(VARIABLES.IN_ISAMEXINT, boolean), VARIABLES.IN_PRIBUSINESS);  
        VARIABLES.update_count = VARIABLES.ROWCOUNT;  
        IF(VARIABLES.update_count = 1)  
        BEGIN  
            SELECT * FROM XmlModel.goodResultsDocument;  
        END  
        ELSE  
        BEGIN  
            SELECT * FROM XmlModel.badResultsDocument;  
        END  
    EXCEPTION e  
        BEGIN  
            RAISE SQLWARNING e.EXCEPTION;  
            SELECT * FROM XmlModel.badResultsDocument;  
        END  
    END