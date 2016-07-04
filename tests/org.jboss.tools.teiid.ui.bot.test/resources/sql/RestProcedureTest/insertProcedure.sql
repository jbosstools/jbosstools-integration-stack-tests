CREATE VIRTUAL PROCEDURE  
BEGIN  
    DECLARE integer VARIABLES.update_count = 0;  
    BEGIN  
        INSERT INTO PartsView.PARTS 
        (PartsView.PARTS.PART_ID, PartsView.PARTS.PART_NAME, PartsView.PARTS.PART_COLOR, PartsView.PARTS.PART_WEIGHT) 
        VALUES 
        (PartsView.AddPart.id, PartsView.AddPart.name, PartsView.AddPart.color,  PartsView.AddPart.weight);  
        
        VARIABLES.update_count = VARIABLES.ROWCOUNT;  
        IF(VARIABLES.update_count = 1)  
        BEGIN  
            SELECT XMLELEMENT(NAME response, 'Operation Successful!');  
        END  
        ELSE  
        BEGIN  
            SELECT XMLELEMENT(NAME response, 'Operation Failed!');  
        END  
    	EXCEPTION e  
        BEGIN  
	        RAISE SQLWARNING e.EXCEPTION;
            SELECT XMLELEMENT(NAME response, 'Operation Failed!');  
        END  
    END  
END