BEGIN 
SELECT 
	XMLELEMENT(NAME Supplier, XMLAGG(XMLELEMENT(NAME Supplier, XMLFOREST(REST_WarView.viewSupplier.SUPPLIER_ID, REST_WarView.viewSupplier.SUPPLIER_NAME, REST_WarView.viewSupplier.SUPPLIER_STATUS,  REST_WarView.viewSupplier.SUPPLIER_CITY, REST_WarView.viewSupplier.SUPPLIER_STATE)))) 				
AS 
	result 
FROM 
	REST_WarView.viewSupplier 
WHERE 
	REST_WarView.viewSupplier.SUPPLIER_NAME = REST_WarView.getSupplierByName.nameIN; 
END

