DECLARE integer VARIABLES.delete_count = 0;  
DELETE FROM RelationalModel.ProductInfo WHERE RelationalModel.ProductInfo.INSTR_ID = VARIABLES.IN_INSTR_ID;  
VARIABLES.delete_count = VARIABLES.ROWCOUNT;  
IF(VARIABLES.delete_count = 1)  
	BEGIN  
        SELECT * FROM XmlModel.goodResultsDocument;  
    END  
ELSE  
    BEGIN  
        SELECT * FROM XmlModel.badResultsDocument;  
    END    