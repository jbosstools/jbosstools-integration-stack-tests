
SET NAMESPACE 'http://teiid.org/rest' AS REST;

CREATE VIEW ProductsInfo (
	INSTR_ID string(10) NOT NULL,
	SYMBOL_TYPE integer OPTIONS(CASE_SENSITIVE 'FALSE', FIXED_LENGTH 'TRUE', SEARCHABLE 'ALL_EXCEPT_LIKE'),
	SYMBOL string(10) NOT NULL,
	CUSIP string(10),
	NAME string(60),
	TYPE string(15),
	ISSUER string(10),
	EXCHANGE string(10),
	ISDJI boolean NOT NULL OPTIONS(CASE_SENSITIVE 'FALSE', FIXED_LENGTH 'TRUE', SEARCHABLE 'ALL_EXCEPT_LIKE'),
	ISSP500 boolean NOT NULL OPTIONS(CASE_SENSITIVE 'FALSE', FIXED_LENGTH 'TRUE', SEARCHABLE 'ALL_EXCEPT_LIKE'),
	ISNAS100 boolean NOT NULL OPTIONS(CASE_SENSITIVE 'FALSE', FIXED_LENGTH 'TRUE', SEARCHABLE 'ALL_EXCEPT_LIKE'),
	ISAMEXINT boolean NOT NULL OPTIONS(CASE_SENSITIVE 'FALSE', FIXED_LENGTH 'TRUE', SEARCHABLE 'ALL_EXCEPT_LIKE'),
	PRIBUSINESS string(30)
) OPTIONS(UPDATABLE 'TRUE') 
AS
	SELECT
		viewRestProcedureSource.PRODUCTSYMBOLS.INSTR_ID, viewRestProcedureSource.PRODUCTSYMBOLS.SYMBOL_TYPE, viewRestProcedureSource.PRODUCTSYMBOLS.SYMBOL, viewRestProcedureSource.PRODUCTSYMBOLS.CUSIP, viewRestProcedureSource.PRODUCTDATA.NAME, viewRestProcedureSource.PRODUCTDATA.TYPE, viewRestProcedureSource.PRODUCTDATA.ISSUER, viewRestProcedureSource.PRODUCTDATA.EXCHANGE, viewRestProcedureSource.PRODUCTDATA.ISDJI, viewRestProcedureSource.PRODUCTDATA.ISSP500, viewRestProcedureSource.PRODUCTDATA.ISNAS100, viewRestProcedureSource.PRODUCTDATA.ISAMEXINT, viewRestProcedureSource.PRODUCTDATA.PRIBUSINESS
	FROM
		viewRestProcedureSource.PRODUCTSYMBOLS, viewRestProcedureSource.PRODUCTDATA
	WHERE
		viewRestProcedureSource.PRODUCTSYMBOLS.INSTR_ID = viewRestProcedureSource.PRODUCTDATA.INSTR_ID ;

CREATE TRIGGER ON ProductsInfo INSTEAD OF INSERT AS 
	FOR EACH ROW
BEGIN ATOMIC
	DECLARE integer VARIABLES.KOUNT;
	DECLARE integer VARIABLES.ROWS_UPDATED;
	IF(("NEW".INSTR_ID IS NULL) OR ("NEW".ISDJI IS NULL) OR ("NEW".ISSP500 IS NULL) OR ("NEW".ISAMEXINT IS NULL) OR ("NEW".ISNAS100 IS NULL) OR ("NEW".SYMBOL IS NULL) OR ("NEW".NAME IS NULL))
	BEGIN
		RAISE SQLEXCEPTION 'The<undefined>following<undefined>elements<undefined>Instr_ID,<undefined>ISDJI,<undefined>ISSP500,<undefined>ISAMEXINT,<undefined>ISNAS100,<undefined>Symbol,<undefined>and<undefined>Name<undefined>are<undefined>not<undefined>nullable,<undefined>non-null<undefined>values<undefined>should<undefined>be<undefined>inserted<undefined>for<undefined>these<undefined>elements.';
	END
	ELSE
	BEGIN
		VARIABLES.KOUNT = (SELECT COUNT(*) FROM viewRestProcedureSource.PRODUCTSYMBOLS WHERE viewRestProcedureSource.PRODUCTSYMBOLS.SYMBOL = "NEW".SYMBOL);
		IF(VARIABLES.KOUNT > 0)
		BEGIN
			RAISE SQLEXCEPTION 'Specified<undefined>symbol<undefined>already<undefined>exists<undefined>in<undefined>database<undefined>-<undefined>Choose<undefined>another<undefined>symbol<undefined>to<undefined>insert.';
		END
		ELSE
		BEGIN
			VARIABLES.KOUNT = (SELECT COUNT(*) FROM viewRestProcedureSource.PRODUCTDATA WHERE viewRestProcedureSource.PRODUCTDATA.INSTR_ID = "NEW".INSTR_ID);
			IF(VARIABLES.KOUNT > 0)
			BEGIN
				RAISE SQLEXCEPTION 'Specified<undefined>instrument<undefined>ID<undefined>already<undefined>exists<undefined>in<undefined>database<undefined>-<undefined>Choose<undefined>another<undefined>Instr_ID<undefined>to<undefined>insert.';
			END
			ELSE
			BEGIN
				VARIABLES.KOUNT = (SELECT COUNT(*) FROM viewRestProcedureSource.PRODUCTDATA WHERE viewRestProcedureSource.PRODUCTDATA.NAME = "NEW".NAME);
				IF(VARIABLES.KOUNT > 0)
				BEGIN
					RAISE SQLEXCEPTION 'Specified<undefined>Name<undefined>already<undefined>exists<undefined>in<undefined>database<undefined>-<undefined>Choose<undefined>another<undefined>Name<undefined>to<undefined>insert.';
				END
				ELSE
				BEGIN
					INSERT INTO viewRestProcedureSource.PRODUCTDATA (viewRestProcedureSource.PRODUCTDATA.INSTR_ID, viewRestProcedureSource.PRODUCTDATA.NAME, viewRestProcedureSource.PRODUCTDATA.TYPE, viewRestProcedureSource.PRODUCTDATA.ISSUER, viewRestProcedureSource.PRODUCTDATA.EXCHANGE, viewRestProcedureSource.PRODUCTDATA.ISDJI, viewRestProcedureSource.PRODUCTDATA.ISSP500, viewRestProcedureSource.PRODUCTDATA.ISNAS100, viewRestProcedureSource.PRODUCTDATA.ISAMEXINT, viewRestProcedureSource.PRODUCTDATA.PRIBUSINESS) VALUES ("NEW".INSTR_ID, "NEW".NAME, "NEW".TYPE, "NEW".ISSUER, "NEW".EXCHANGE, "NEW".ISDJI, "NEW".ISSP500, "NEW".ISNAS100, "NEW".ISAMEXINT, "NEW".PRIBUSINESS);
					INSERT INTO viewRestProcedureSource.PRODUCTSYMBOLS (viewRestProcedureSource.PRODUCTSYMBOLS.INSTR_ID, viewRestProcedureSource.PRODUCTSYMBOLS.SYMBOL_TYPE, viewRestProcedureSource.PRODUCTSYMBOLS.SYMBOL, viewRestProcedureSource.PRODUCTSYMBOLS.CUSIP) VALUES ("NEW".INSTR_ID, "NEW".SYMBOL_TYPE, "NEW".SYMBOL, "NEW".CUSIP);
				END
			END
		END
	END
END

CREATE TRIGGER ON ProductsInfo INSTEAD OF DELETE AS 
	FOR EACH ROW
BEGIN ATOMIC
	DELETE FROM viewRestProcedureSource.PRODUCTSYMBOLS WHERE viewRestProcedureSource.PRODUCTSYMBOLS.INSTR_ID = "OLD".INSTR_ID;
	DELETE FROM viewRestProcedureSource.PRODUCTDATA WHERE viewRestProcedureSource.PRODUCTDATA.INSTR_ID = "OLD".INSTR_ID;
END

CREATE VIRTUAL PROCEDURE getProduct (IN instr_id string(10)) RETURNS TABLE (result xml)
 OPTIONS("REST:URI" 'product/{instr_id}', "REST:METHOD" 'GET')
	AS
BEGIN
	SELECT XMLELEMENT(NAME Products, XMLAGG(XMLELEMENT(NAME Product, XMLFOREST(viewRestProcedureView.ProductsInfo.INSTR_ID, viewRestProcedureView.ProductsInfo.SYMBOL_TYPE, viewRestProcedureView.ProductsInfo.SYMBOL, viewRestProcedureView.ProductsInfo.CUSIP, viewRestProcedureView.ProductsInfo.NAME, viewRestProcedureView.ProductsInfo.TYPE, viewRestProcedureView.ProductsInfo.ISSUER, viewRestProcedureView.ProductsInfo.EXCHANGE, viewRestProcedureView.ProductsInfo.ISDJI, viewRestProcedureView.ProductsInfo.ISSP500, viewRestProcedureView.ProductsInfo.ISNAS100, viewRestProcedureView.ProductsInfo.ISAMEXINT, viewRestProcedureView.ProductsInfo.PRIBUSINESS)))) AS result FROM viewRestProcedureView.ProductsInfo WHERE viewRestProcedureView.ProductsInfo.INSTR_ID = viewRestProcedureView.getProduct.instr_id;
END;

CREATE VIRTUAL PROCEDURE addProduct (IN instr_id string(10), IN symbol string(10), IN name string(60), IN isdji boolean, IN issp500 boolean, IN isnas100 boolean, IN isamexint boolean) RETURNS TABLE (expr1 xml)
 OPTIONS("REST:URI" 'product/', "REST:METHOD" 'POST')
	AS
BEGIN
	DECLARE integer VARIABLES.update_count = 0;
	BEGIN
		INSERT INTO viewRestProcedureView.ProductsInfo (viewRestProcedureView.ProductsInfo.INSTR_ID, viewRestProcedureView.ProductsInfo.SYMBOL, viewRestProcedureView.ProductsInfo.NAME, viewRestProcedureView.ProductsInfo.ISDJI, viewRestProcedureView.ProductsInfo.ISSP500, viewRestProcedureView.ProductsInfo.ISNAS100, viewRestProcedureView.ProductsInfo.ISAMEXINT) VALUES (viewRestProcedureView.addProduct.instr_id, viewRestProcedureView.addProduct.symbol, viewRestProcedureView.addProduct.name, viewRestProcedureView.addProduct.isdji, viewRestProcedureView.addProduct.issp500, viewRestProcedureView.addProduct.isnas100, viewRestProcedureView.addProduct.isamexint);
		VARIABLES.update_count = VARIABLES.ROWCOUNT;
		IF(VARIABLES.update_count = 1)
		BEGIN
			SELECT XMLELEMENT(NAME response, 'Operation<undefined>Successful!');
		END
		ELSE
		BEGIN
			SELECT XMLELEMENT(NAME response, 'Operation<undefined>Failed!');
		END
	EXCEPTION e
		BEGIN
			RAISE SQLWARNING e.EXCEPTION;
			SELECT XMLELEMENT(NAME response, 'Operation<undefined>Failed!');
		END
	END
END;

