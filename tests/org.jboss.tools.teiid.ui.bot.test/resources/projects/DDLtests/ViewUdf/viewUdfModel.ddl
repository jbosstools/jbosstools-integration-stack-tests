
SET NAMESPACE 'http://teiid.org/rest' AS REST;


CREATE VIRTUAL PROCEDURE testProc (
	IN p1 string(4000)
) RETURNS
	TABLE (
		xml_out xml
) OPTIONS(UPDATECOUNT '1')
	AS
BEGIN
 SELECT XMLELEMENT(NAME test, XMLFOREST(viewUdfModel.testProc.p1 AS c1, 'c2' AS c2)) AS xml_out;
END;

CREATE VIRTUAL FUNCTION udfConcatNull (
	IN stringLeft string(2000),
	IN stringRight string(2000),
	OUT concatenation string(4000) RESULT
) RETURNS
	TABLE (
		workAround string(2000)
) OPTIONS("FUNCTION-CATEGORY" 'MY_TESTING_FUNCTION_CATEGORY', JAVA_CLASS 'userdefinedfunctions.MyConcatNull', JAVA_METHOD 'myConcatNull');

