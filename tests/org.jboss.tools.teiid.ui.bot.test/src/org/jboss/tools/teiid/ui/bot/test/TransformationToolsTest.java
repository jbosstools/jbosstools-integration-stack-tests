package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.AssertBot;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ProcedureDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * tested features:
 * - add transformation sources (add menu action, paste SQL, type SQL)
 * - reconciler 
 * 		- included: expand SELECT, expression builder from reconciler and from transformation editor
 * - criteria builder
 * 		- included: expression builder from criteria builder
 * - comments in transformation
 * - SQL templates
 * 		- all templates were verified manually
 * 		- for every template, checks if its selecting sets right transformation
 *  	- for one template, checks if generated table in model contains transformation
 *  	- note: if template has changed, test would fail (so test it manually again and update test accordingly) 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class TransformationToolsTest {
	private static final String PROJECT_NAME = "TransformationToolsProject";
	private static final String VIEW_MODEL = "PartsView.xmi";
	
	private ModelExplorer modelExplorer;

	@BeforeClass
	public static void before(){
		new WorkbenchShell().maximize();
	}
	
	@Before
	public void importProject() {
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);
	}
	
	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void addTransformationSources(){
		// add transformation menu action
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL);
	    new TableDialog(true)
	    		.setName("AddTransformation")
	    		.finish();	
	    
	    RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "AddTransformation");
		modelExplorer.selectItem(PROJECT_NAME, "PartsSupplier.xmi", "SUPPLIER");
		new ContextMenu("Modeling","Add Transformation Source(s)").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.activate();
		editor.save();
		AbstractWait.sleep(TimePeriod.SHORT);
		AssertBot.transformationContains(transformationEditor.getTransformation(), "Supplier.SUPPLIER");
		
		List<String> sourceAttrs = new ArrayList<>();
		sourceAttrs.addAll(editor.listTableAttributes("SUPPLIER"));
		List<String> viewAttrs = new ArrayList<>();
		viewAttrs.addAll(editor.listTableAttributes("AddTransformation"));		
		for(String sourceAtt : sourceAttrs){
			assertTrue(viewAttrs.contains(sourceAtt));
		}
		editor.returnToParentDiagram();
		ProblemsViewEx.checkErrors();
		
		// paste transformation
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL);
	    new TableDialog(true)
	    		.setName("PasteTransformation")
	    		.finish();
		
	    editor.activate();
	    transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "PasteTransformation");
		transformationEditor.insertAndValidateSql("SELECT * FROM PartsSupplier.SUPPLIER, PartsSupplier.PARTS, PartsSupplier.SUPPLIER_PARTS");
		editor.save();
		
		sourceAttrs = new ArrayList<>();
		sourceAttrs.addAll(editor.listTableAttributes("SUPPLIER"));
		sourceAttrs.addAll(editor.listTableAttributes("PARTS"));		
		sourceAttrs.addAll(editor.listTableAttributes("SUPPLIER_PARTS"));
		viewAttrs = new ArrayList<>();
		viewAttrs.addAll(editor.listTableAttributes("PasteTransformation"));		
		for(String sourceAtt : sourceAttrs){
			assertTrue(viewAttrs.contains(sourceAtt));
		}	
		editor.returnToParentDiagram();
		ProblemsViewEx.checkErrors();
		
		// type transformation
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL);
	    new TableDialog(true)
	    		.setName("TypeTransformation")
	    		.finish();	
		
		editor.activate();
		transformationEditor = editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "TypeTransformation");
		transformationEditor.typeTransformation("SELECT * FROM PartsSupplier.PARTS");
		editor.save();
		
		sourceAttrs = new ArrayList<>();
		sourceAttrs.addAll(editor.listTableAttributes("PARTS"));
		viewAttrs = new ArrayList<>();
		viewAttrs.addAll(editor.listTableAttributes("TypeTransformation"));
		for(String sourceAtt : sourceAttrs){
			assertTrue(viewAttrs.contains(sourceAtt));
		}
		ProblemsViewEx.checkErrors();
	}                             

	@Test
	public void testReconciler(){
		modelExplorer.openModelEditor(PROJECT_NAME, VIEW_MODEL);
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
		
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "ReconcilerTable");
	    transformationEditor.expandSelect();
		String expanededSelect = "PartsSupplier.PARTS.PART_ID, PartsSupplier.PARTS.PART_NAME, PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_WEIGHT";
		AssertBot.transformationContains(transformationEditor.getTransformation(), expanededSelect);
		
		editor.activate();
		editor.deleteAttribute("ReconcilerTable", ModelEditor.ItemType.TABLE, "PART_NAME", false);
		editor.renameAttribute("ReconcilerTable", ModelEditor.ItemType.TABLE, "PART_COLOR", "COLOR_NAME");
		editor.renameAttribute("ReconcilerTable", ModelEditor.ItemType.TABLE, "PART_ID", "ID");
		editor.setAttributeDataType("ReconcilerTable", ModelEditor.ItemType.TABLE, "COLOR_NAME", "string", 285);
		editor.setAttributeDataType("ReconcilerTable", ModelEditor.ItemType.TABLE, "PART_WEIGHT", "bigdecimal", null);
		editor.save();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		ReconcilerDialog reconciler = transformationEditor.openReconciler();
		reconciler.bindAttributes("ID : string","PART_ID");
		
		ExpressionBuilderDialog expressionBuilder2 = reconciler.openExpressionBuilder("COLOR_NAME : string");
		expressionBuilder2.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.FUNCTION)
				.selectFunctionCategory("String")
				.selectFunctionValue("CONCAT(STRING1, STRING2)")
				.apply()
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.COLUMN)
				.selectColumnAttribute("PartsSupplier.PARTS", "PART_COLOR")
				.apply()
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.COLUMN)
				.selectColumnAttribute("PartsSupplier.PARTS", "PART_NAME")
				.apply();
		expressionBuilder2.finish();
		
		reconciler.activate();
		reconciler.resolveTypes(ReconcilerDialog.ResolverType.KEEP_VIRTUAL_TARGET);
		reconciler.activate();
		reconciler.clearRemainingUnmatchedSymbols();
		reconciler.finish();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		expanededSelect = "PartsSupplier.PARTS.PART_ID AS ID, CONCAT(PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_NAME) "
				+ "AS COLOR_NAME, convert(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) AS PART_WEIGHT";
		AssertBot.transformationContains(transformationEditor.getTransformation(), expanededSelect);
	
		transformationEditor.setCoursorPositionInTransformation(47);	
		
		ExpressionBuilderDialog expressionBuilder3 = transformationEditor.openExpressionBuilder();
		expressionBuilder3.selectTreeViewItem("CONCAT(PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_NAME)","PartsSupplier.PARTS.PART_COLOR")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.FUNCTION)
				.selectFunctionCategory("String")
				.selectFunctionValue("CONCAT(STRING1, STRING2)")
				.apply()
				.selectTreeViewItem("CONCAT(CONCAT(PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_NAME), PartsSupplier.PARTS.PART_NAME)",
						"CONCAT(PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_NAME)","PartsSupplier.PARTS.PART_NAME")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.CONSTANT)
				.selectConstantType("string")
				.selectConstantValue(" ")
				.apply();
		expressionBuilder3.finish();
		
		new WorkbenchShell();
		AbstractWait.sleep(TimePeriod.SHORT);
		expanededSelect = "CONCAT(CONCAT(PartsSupplier.PARTS.PART_COLOR, ' '), PartsSupplier.PARTS.PART_NAME)";
		AssertBot.transformationContains(transformationEditor.getTransformation(), expanededSelect);
		
		editor.save();		
		ProblemsViewEx.checkErrors();
	}
	
	@Test
	public void testCriteriaBuilder(){			
		modelExplorer.openModelEditor(PROJECT_NAME, VIEW_MODEL);
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
		
		TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "CriteriaBuilderTable");
		CriteriaBuilderDialog criteriaBuilder = transformationEditor.openCriteriaBuilder();
		criteriaBuilder.selectAttribute("PartsSupplier.PARTS", "PART_ID",CriteriaBuilderDialog.CriteriaSide.LEFT)
				.selectAttribute("PartsSupplier.SUPPLIER_PARTS", "PART_ID",CriteriaBuilderDialog.CriteriaSide.RIGHT)
				.selectOperator(CriteriaBuilderDialog.OperatorType.EQUALS)
				.apply()
				.clickOperator(CriteriaBuilderDialog.OperatorType.AND)
				.selectAttribute("PartsSupplier.SUPPLIER", "SUPPLIER_ID",CriteriaBuilderDialog.CriteriaSide.LEFT)
				.selectAttribute("PartsSupplier.SUPPLIER_PARTS", "SUPPLIER_ID",CriteriaBuilderDialog.CriteriaSide.RIGHT)
				.selectOperator(CriteriaBuilderDialog.OperatorType.EQUALS)
				.apply()
				.selectTreeViewItem("(PartsSupplier.PARTS.PART_ID = PartsSupplier.SUPPLIER_PARTS.PART_ID) "
						+ "AND (PartsSupplier.SUPPLIER.SUPPLIER_ID = PartsSupplier.SUPPLIER_PARTS.SUPPLIER_ID)",
						"PartsSupplier.SUPPLIER.SUPPLIER_ID = PartsSupplier.SUPPLIER_PARTS.SUPPLIER_ID")
				.clickOperator(CriteriaBuilderDialog.OperatorType.AND)
				.selectRadioButton(CriteriaBuilderDialog.RadioButtonType.FUNCTION, CriteriaBuilderDialog.CriteriaSide.LEFT);
		
		ExpressionBuilderDialog expressionBuilder = criteriaBuilder.openFunctionBuilder(CriteriaBuilderDialog.CriteriaSide.LEFT);
		expressionBuilder.selectFunctionCategory("Numeric")
				.selectFunctionValue("(OP1*OP2)")
				.apply()
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.FUNCTION)
				.selectFunctionCategory("Numeric")
				.selectFunctionValue("(OP1/OP2)")
				.apply()
				.selectTreeViewItem("((<undefined> / <undefined>) * <undefined>)","(<undefined> / <undefined>)","<undefined>")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.FUNCTION)
				.selectFunctionCategory("Conversion")
				.selectFunctionValue("CONVERT(VALUE, TARGET)")
				.apply()
				.selectTreeViewItem("((CONVERT(<undefined>, string) / <undefined>) * <undefined>)", 
						"(CONVERT(<undefined>, string) / <undefined>)","CONVERT(<undefined>, string)","<undefined>")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.COLUMN)
				.selectColumnAttribute("PartsSupplier.PARTS", "PART_WEIGHT")
				.apply()
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.CONSTANT)
				.selectConstantType("bigdecimal")
				.apply()
				.selectTreeViewItem("((CONVERT(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) / <undefined>) * <undefined>)",
						"(CONVERT(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) / <undefined>)","<undefined>")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.CONSTANT)
				.selectConstantType("bigdecimal")
				.selectConstantValue("1000")
				.apply()
				.selectTreeViewItem("((CONVERT(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) / 1000) * <undefined>)","<undefined>")
				.selectRadioButton(ExpressionBuilderDialog.RadioButtonType.COLUMN)
				.selectColumnAttribute("PartsSupplier.SUPPLIER_PARTS", "QUANTITY")
				.apply();
		expressionBuilder.finish();
		
		criteriaBuilder.activate();
		criteriaBuilder.selectRadioButton(CriteriaBuilderDialog.RadioButtonType.CONSTANT, CriteriaBuilderDialog.CriteriaSide.RIGHT)
				.selectConstantType("bigdecimal", 0)
				.selectConstantValue("10", 0)
				.selectOperator(CriteriaBuilderDialog.OperatorType.LT)
				.apply();
		String expectedSql = new ResourceFileHelper().getSql("TransformationToolsTest/expectedWhere").replaceAll("\r|\n", "");
		assertEquals(expectedSql, criteriaBuilder.getCurrentSqlContent());	
		criteriaBuilder.finish();
		
		AssertBot.transformationContains(transformationEditor.getTransformation(), "WHERE");
		AssertBot.transformationContains(transformationEditor.getTransformation(), expectedSql);
		editor.save();
		AbstractWait.sleep(TimePeriod.SHORT);
		ProblemsViewEx.checkErrors();		
	}

	@Test
	public void testCommentsInTransformation(){
		modelExplorer.openModelEditor(PROJECT_NAME, VIEW_MODEL);
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
		
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "ReconcilerTable");
	    String sqlWithComment = new ResourceFileHelper().getSql("TransformationToolsTest/reconcilerComment").replaceAll("\r|\n", "");
	    transformationEditor.insertAndValidateSql(sqlWithComment);
	    editor.save();
	    AssertBot.transformationContains(transformationEditor.getTransformation(), "/* First comment in the reconciler table */");
		AssertBot.transformationContains(transformationEditor.getTransformation(), "/* Second comment in the reconciler table */");
		transformationEditor.close();
		editor.returnToParentDiagram();
		editor.activate();
		
		transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "CriteriaBuilderTable");
	    sqlWithComment = new ResourceFileHelper().getSql("TransformationToolsTest/criteriaBuilderComment").replaceAll("\r|\n", "");
	    transformationEditor.insertAndValidateSql(sqlWithComment);
	    editor.save();
		AssertBot.transformationContains(transformationEditor.getTransformation(), "/* First comment in the criteria builder table */");
		AssertBot.transformationContains(transformationEditor.getTransformation(), "/* Second comment in the criteria builder table */");
		transformationEditor.close();
		editor.returnToParentDiagram();
		editor.activate();
		
	    if(new JiraClient().isIssueClosed("TEIIDDES-2411")){
	    	//test if comments in the second sql transformation doesn't change comments in the first sql transformation
		    transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "ReconcilerTable");
		    AssertBot.transformationContains(transformationEditor.getTransformation(), "/* First comment in the reconciler table */");
			AssertBot.transformationContains(transformationEditor.getTransformation(), "/* Second comment in the reconciler table */");
	    }
	}

	@Test
	public void testTemplates(){
		// table templates
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL);
		TableDialog dialog = new TableDialog(true);
		dialog.setName("TemplateTable");
		
		dialog.setSqlTemplate("Simple SELECT","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT * FROM [TABLEA]");
		
		dialog.setSqlTemplate("SELECT with Join Criteria","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT [TABLEA.COL1], [TABLEA.COL2], [TABLEB.COL1] FROM [TABLEA], [TABLEB] WHERE [TABLEA.COL1] = [TABLEB.COL1]");
		
		dialog.setSqlTemplate("UNION Query","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT [COL1], [COL2] FROM [TABLEA] UNION SELECT [COL1], [COL2] FROM [TABLEB]");
		
		dialog.setSqlTemplate("Flat File - Local Source","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT A.[Name], A.[Sport], A.[Position], A.[City] FROM (EXEC [EmployeeFileProcedures].getTextFiles('PlayerData.txt')) AS f,  TEXTTABLE(f.file COLUMNS Name string, Sport string, Position string, City string HEADER 2 SKIP 3) AS A");
		
		dialog.setSqlTemplate("XML File - Local Source","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT A.PMID AS PMID, A.Journal AS Journal, A.Title AS Title FROM (EXEC MP.getTextFiles('medsamp2011.xml')) AS f, XMLTABLE('/MedlineCitationSet/MedlineCitation' PASSING XMLPARSE(DOCUMENT f.file) COLUMNS PMID string PATH '/PMID', Journal string PATH '/Article/Journal', Title string PATH '/Article/ArticleTitle') AS A");
		
		dialog.setSqlTemplate("XML File - URL Source","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), "SELECT A.COMMON AS COMMON, A.BOTANICAL AS BOTANICAL, A.ZONE AS ZONE, A.PRICE AS PRICE FROM (EXEC PlantWSProcedures.invokeHttp('GET', null, 'http://www.w3schools.com/xml/plant_catalog.xml')) AS f, XMLTABLE('/CATALOG/PLANT' PASSING XMLPARSE(DOCUMENT f.result) COLUMNS COMMON string PATH '/COMMON', BOTANICAL string PATH '/BOTANICAL', ZONE string PATH '/ZONE', PRICE string PATH '/PRICE') AS A");
		
		String expectedT = "SELECT A.[col_1], A.[col_2] \nFROM [ObjectTableName] as T, \nOBJECTTABLE('x' PASSING T.[ObjectColumnName] Object as x COLUMNS col_1 string, col_2 string) AS A";
		dialog.setSqlTemplate("OBJECTTABLE() example","Replace all SQL Text");
		AssertBot.transformationEquals(dialog.getTransformationSql(), expectedT);
		
		dialog.finish();
		
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
		String actualT = editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "TemplateTable").getTransformation();
		AssertBot.transformationEquals(actualT, expectedT);
		
		// procedure templates
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL);
		ProcedureDialog pDialog = NewProcedureWizard.createViewProcedure();
		pDialog.setName("TemplateProcedure");
		
		pDialog.setSqlTemplate("Simple Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "BEGIN\n	SELECT * FROM [TABLEA];\nEND");
		
		pDialog.setSqlTemplate("INSERT Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "FOR EACH ROW BEGIN ATOMIC INSERT INTO [TABLEA] ([COL1], [COL2], [COL3]) VALUES (NEW.[COL1], NEW.[COL2], NEW.[COL3]); END");
		
		pDialog.setSqlTemplate("UPDATE Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "FOR EACH ROW BEGIN UPDATE [TABLEA] SET [COL1]=NEW.[COL1], [COL2]=NEW.[COL2], [COL3]=NEW.[COL3] WHERE [PK-KEY-COL]=OLD.[PK-KEY-COL]; END");
		
		pDialog.setSqlTemplate("DELETE Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "FOR EACH ROW BEGIN DELETE FROM [TABLEA] WHERE [PK-KEY COL] = OLD.[PK-KEY-COL]; END");
		
		pDialog.setSqlTemplate("SOAP Web Service - \"Create\" Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "BEGIN SELECT XMLELEMENT(NAME CapitalCity, XMLNAMESPACES(DEFAULT 'http://www.oorsprong.org/websamples.countryinfo'), XMLELEMENT(NAME sCountryISOCode, COUNTRYINFOSERVICEXML.CAPITALCITY.CREATE_CAPITALCITY.sCountryISOCode)) AS xml_out; END");
		
		pDialog.setSqlTemplate("SOAP Web Service - \"Extract\" Procedure","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "BEGIN SELECT employee.* FROM XMLTABLE(XMLNAMESPACES('http://teiid.org' as teiid), '/teiid:getdepartmentResponse/return/employee' PASSING f.result COLUMNS empID integer PATH '@id', firstname string PATH 'name/first', lastname string PATH 'name/last') AS employee; END");
		
		pDialog.setSqlTemplate("REST ProcedureExportTeiidDdl_modelGroupTitle","Replace all SQL Text");
		AssertBot.transformationEquals(pDialog.getTransformationSql(), "BEGIN SELECT XMLELEMENT(NAME authors, XMLAGG(XMLELEMENT(NAME author, XMLFOREST(MySqlBooks.AUTHORS.AUTHOR_ID, MySqlBooks.AUTHORS.FIRSTNAME, MySqlBooks.AUTHORS.LASTNAME, MySqlBooks.AUTHORS.MIDDLEINIT)))) AS result FROM MySqlBooks.AUTHORS WHERE Procedures.GetAuthorByID.author_id = MySqlBooks.AUTHORS.AUTHOR_ID; END");
		
		pDialog.finish();
		
		editor.activate();
		editor.returnToParentDiagram();
		actualT = editor.openTransformationDiagram(ModelEditor.ItemType.PROCEDURE, "TemplateProcedure").getTransformation();
		AssertBot.transformationEquals(actualT, "BEGIN\n	SELECT XMLELEMENT(NAME authors, XMLAGG(XMLELEMENT(NAME author, XMLFOREST(MySqlBooks.AUTHORS.AUTHOR_ID, MySqlBooks.AUTHORS.FIRSTNAME, MySqlBooks.AUTHORS.LASTNAME, MySqlBooks.AUTHORS.MIDDLEINIT)))) AS result FROM MySqlBooks.AUTHORS WHERE Procedures.GetAuthorByID.author_id = MySqlBooks.AUTHORS.AUTHOR_ID;\nEND");	
	}
}
