package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;
import org.jboss.tools.teiid.reddeer.editor.ModelDiagram;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * 
 * tested features:
 * - add transformation sources
 * - criteria builder
 * - expression builder (from criteria b., reconciler, transformation)
 * - expand SELECT
 * - reconciler
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class TransformationToolsTest {
	private static final String PROJECT_NAME = "TransformationToolsProject";
	
	private static ModelExplorer modelExplorer;

	@BeforeClass
	public static void importProject() {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
	}
	
	@Test
	public void test(){
		// 1. Add the transformation sources
		modelExplorer.openModelEditor(PROJECT_NAME,"PartsView.xmi","SupplierParts");
		ModelEditor editor = new ModelEditor("PartsView.xmi");
		editor.showTransformation();
		
		modelExplorer.activate();
		new DefaultTreeItem(PROJECT_NAME,"PartsSupplier.xmi","SUPPLIER").select();
		new ContextMenu("Modeling","Add Transformation Source(s)").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("transformation text not set", editor.getTransformation().replaceAll(" |\t|\n|\r" ,"").equals("SELECT*FROMPartsSupplier.SUPPLIER"));	
		
		editor.setTransformation("SELECT * FROM PartsSupplier.SUPPLIER, PartsSupplier.PARTS, PartsSupplier.SUPPLIER_PARTS");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		modelExplorer.getProject(PROJECT_NAME).refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		
		editor.show();
		List<String> sourceAttrs = new ArrayList<>();
		ModelDiagram supplier = editor.getModelDiagram("SUPPLIER", "Transformation Diagram");
		supplier.select();
		sourceAttrs.addAll(supplier.getModelAttributes());
		ModelDiagram parts = editor.getModelDiagram("PARTS", "Transformation Diagram");
		parts.select();
		sourceAttrs.addAll(parts.getModelAttributes());		
		ModelDiagram sp = editor.getModelDiagram("SUPPLIER_PARTS", "Transformation Diagram");
		sp.select();
		sourceAttrs.addAll(sp.getModelAttributes());
		List<String> viewAttrs = new ArrayList<>();
		ModelDiagram view = editor.getModelDiagram("SupplierParts", "Transformation Diagram");
		view.select();
		viewAttrs.addAll(view.getModelAttributes());		
		for(String sourceAtt : sourceAttrs){
			assertTrue(viewAttrs.contains(sourceAtt));
		}	
		new ProblemsViewEx().checkErrors();
		
		// 2. Create a WHERE clause using Criteria Builder
		editor.show();
		CriteriaBuilderDialog criteriaBuilder = editor.openCriteriaBuilder();
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
		
		String expectedSql = "(PartsSupplier.PARTS.PART_ID = PartsSupplier.SUPPLIER_PARTS.PART_ID) AND ((PartsSupplier.SUPPLIER.SUPPLIER_ID = PartsSupplier.SUPPLIER_PARTS.SUPPLIER_ID) "
				+ "AND (((CONVERT(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) / 1000) * PartsSupplier.SUPPLIER_PARTS.QUANTITY) < 10))";
		assertEquals(expectedSql, criteriaBuilder.getCurrentSqlContent());	
		criteriaBuilder.finish();
		
		assertTrue(editor.getTransformation().contains("WHERE"));
		assertTrue(editor.getTransformation().contains(expectedSql));
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		modelExplorer.getProject(PROJECT_NAME);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		new ProblemsViewEx().checkErrors();		
		
		// 3. Create a new view table and use the Reconciler to map columns
	    modelExplorer.addChildToModelItem(ChildType.TABLE, PROJECT_NAME, "PartsView.xmi");
		new DefaultShell("Create Relational View Table");
		new LabeledText("Name").setText("AltParts");
		new PushButton("OK").click();	
		
		modelExplorer.openModelEditor(PROJECT_NAME,"PartsView.xmi","AltParts");
		editor = new ModelEditor("PartsView.xmi");
		new WorkbenchShell();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.showTransformation();
		new WorkbenchShell();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.typeTransformation("SELECT * FROM PartsSupplier.PARTS");
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		sourceAttrs = new ArrayList<>();
		ModelDiagram source = editor.getModelDiagram("PARTS", "Transformation Diagram");
		source.select();
		sourceAttrs.addAll(source.getModelAttributes());	
		viewAttrs = new ArrayList<>();
		view = editor.getModelDiagram("AltParts", "Transformation Diagram");
		view.select();
		viewAttrs.addAll(view.getModelAttributes());		
		for(String sourceAtt : sourceAttrs){
			assertTrue(viewAttrs.contains(sourceAtt));
		}
		 
		editor.clickButtonOnToolbar("Expand SELECT * ");
		editor.getTransformation().contains("PartsSupplier.PARTS.PART_ID, PartsSupplier.PARTS.PART_NAME, PartsSupplier.PARTS.PART_COLOR, PartsSupplier.PARTS.PART_WEIGHT");
		
		editor.deleteColumnFromTable("AltParts", "PART_NAME", false);
		editor.renameColumn("AltParts", "PART_COLOR", "COLOR_NAME");
		editor.setDataTypeToColumn("AltParts", "COLOR_NAME", "string", 285);
		editor.setDataTypeToColumn("AltParts", "PART_WEIGHT", "bigdecimal", null);
		editor.renameColumn("AltParts", "PART_ID", "ID");
		
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		ReconcilerDialog reconciler = editor.openReconciler();
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
		assertTrue(editor.getTransformation().contains("PartsSupplier.PARTS.PART_ID AS ID, CONCAT(PartsSupplier.PARTS.PART_COLOR, "
				+ "PartsSupplier.PARTS.PART_NAME) AS COLOR_NAME, convert(PartsSupplier.PARTS.PART_WEIGHT, bigdecimal) AS PART_WEIGHT"));
	
		editor.setCoursorPositionInTransformation(47);	
		
		ExpressionBuilderDialog expressionBuilder3 = editor.openExpressionBuilder();
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
		assertTrue(editor.getTransformation().contains("CONCAT(CONCAT(PartsSupplier.PARTS.PART_COLOR, ' '), PartsSupplier.PARTS.PART_NAME)"));
		
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		new ProblemsViewEx().checkErrors();
	}
}
