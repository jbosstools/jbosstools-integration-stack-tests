package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.core.matcher.WithLabelMatcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;

public class CriteriaBuilder {
	public static class CriteriaSide {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
	public static class RadioButtonType {
		public static final String COLUMN = "Show the Column Editor";
		public static final String CONSTANT = "Show the Constant Editor";
		public static final String FUNCTION = "Show the Function Display Editor";
	}
	public static class OperatorType {
		public static final int EQUALS = 0;// =
		public static final int NOT_EQUAL = 1;// <>
		public static final int LT = 2;// <
		public static final int GT = 3;// >
		public static final int LESS_OR_EQUAL = 4;// <=
		public static final int GREATER_OR_EQUAL = 5;// >=
		public static final int IS_NULL = 6;// IS NULL
		public static final int LIKE = 7;// LIKE
		public static final int IN = 8;// IN		
		public static final String AND = "AND";
		public static final String OR = "OR";
		public static final String NOT = "NOT";
	}
	
	public CriteriaBuilder() {
		activate();
	}

	public CriteriaBuilder activate(){
		new DefaultShell("Criteria Builder");
		new DefaultCTabItem("Tree View").activate();
		return this;
	}

	/**
	 * @param type - CriteriaBuilder.RadioButtonType.COLUMN|CONSTANT|FUNCTION
	 * @param leftRight - CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 */
	public void selectRadioButton(String type, int leftRight) {
		new RadioButton(leftRight,new ButtonWithToolTipMatcher(type)).click();
	}
	
	/**
	 * apply criteria
	 */
	public void apply() {
		new PushButton("Apply").click();
	}

	/**
	 * (combo box)
	 * @param operatorType - CriteriaBuilder.OperatorType.EQUALS|NOT_EQUAL|LT|GT|...
	 */
	public void selectOperator(int operatorType) {
		new DefaultCombo(0).setSelection(operatorType);
	}
	
	/**
	 * (button)
	 * @param operatorType - CriteriaBuilder.OperatorType.AND|OR|NOT
	 */
	public void clickOperator(String operatorType) {
		new PushButton(operatorType).click();
	}
	
	/**
	 * clear already set criteria
	 */
	public void clearCriteria() {
		new PushButton("Delete").click();
	}
	
	/**
	 * select item in Tree View
	 * @param itemPath
	 */
	public void selectTreeViewItem(String... itemPath) {
		new DefaultCTabItem("Tree View").activate();
		new DefaultTreeItem(itemPath).select();
	}
	
	/**
	 * get current text from SQL View
	 * @param itemPath
	 */
	public String getCurrentSqlContent() {
		DefaultCTabItem tab = new DefaultCTabItem("SQL View");
		tab.activate();			
		return new DefaultStyledText().getText();
	}
	
	/**
	 * select column attribute (for relational sources)
	 * @param index - if column is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0 
	 */
	public void selectAttribute(String table, String attribute, int index) {
		DefaultTree tree = new DefaultTree(index + 1);
		tree.setFocus();
		new DefaultTreeItem(tree, table, table + "." + attribute).select();
	}
	
	/**
	 * for XML sources
	 */
	public void selectXmlAttribute(String document, String element, int index) {
		// TODO impl. this according relational if needed
//			shell.bot().tree(index).setFocus();
//			shell.bot().tree(index).expandNode(document).select(element);
	}
	
	/** 
	 * open function expression builder
	 * @param index - if function is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public ExpressionBuilder editFunction(int index) {
		return new ExpressionBuilder(index);	
	}
	
	/**
	 * set value in type combo box under constant editor
	 * @param index - if constant is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public void selectConstantType(String type, int index){
		new DefaultCombo(index,new WithLabelMatcher("Type:")).setSelection(type);
	}
	
	/**
	 * set value in value text field under constant editor
	 * @param index - if constant is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public void selectConstantValue(String value, int index){
		new DefaultText(new DefaultGroup("Value"),index).setText(value);
	}

	/**
	 * finish criteria builder
	 */
	public void finish() {
		new PushButton("OK").click();
	}
}
