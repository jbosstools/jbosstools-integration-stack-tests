package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithLabelMatcher;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class CriteriaBuilderDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(CriteriaBuilderDialog.class);
	
	public static class CriteriaSide {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
	public static class RadioButtonType {
        public static final String COLUMN = "Column";
        public static final String CONSTANT = "Constant";
        public static final String FUNCTION = "Function";
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
	
	public CriteriaBuilderDialog() {
		super("Criteria Builder");
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
	}
	@Override
	public void activate(){
		super.activate();
		new DefaultCTabItem("Tree View").activate();
	}

	/**
	 * Selects specified radio button.
	 * @param type - CriteriaBuilder.RadioButtonType.COLUMN|CONSTANT|FUNCTION
	 * @param leftRight - CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 */
    public CriteriaBuilderDialog selectRadioButton(String type, int leftRight) {
		log.info("Selecting " + type + " radio button (index=" + leftRight + ")");
        new RadioButton(leftRight, new WithMnemonicTextMatcher(type)).click();
		return this;
	}
	
	/**
	 * Applies built criteria.
	 */
	public CriteriaBuilderDialog apply() {
		log.info("Applying built criteria");
		new PushButton("Apply").click();
		return this;
	}

	/**
	 * Selects specified operator (combo box)
	 * @param operatorType - CriteriaBuilder.OperatorType.EQUALS|NOT_EQUAL|LT|GT|...
	 */
	public CriteriaBuilderDialog selectOperator(int operatorType) {
		log.info("Selecting operator (id=" + operatorType + ")");
		new DefaultCombo(0).setSelection(operatorType);
		return this;
	}
	
	/**
	 * Clicks on specified operator (button)
	 * @param operatorType - CriteriaBuilder.OperatorType.AND|OR|NOT
	 */
	public CriteriaBuilderDialog clickOperator(String operatorType) {
		log.info("Clicking operator '" + operatorType + "'");
		new PushButton(operatorType).click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return this;
	}
	
	/**
	 * Clears already built criteria
	 */
	public CriteriaBuilderDialog clearCriteria() {
		log.info("Clearing built criteria");
		new PushButton("Delete").click();
		return this;
	}
	
	/**
	 * Selects item in Tree View
	 */
	public CriteriaBuilderDialog selectTreeViewItem(String... itemPath) {
		log.info("Selecting item " + Arrays.toString(itemPath) + " in 'Tree View'");
		new DefaultCTabItem("Tree View").activate();
		new DefaultTreeItem(itemPath).select();
		return this;
	}
	
	/**
	 * Gets current text from SQL View
	 */
	public String getCurrentSqlContent() {
		log.info("Getting current sql from 'SQL View'");
		DefaultCTabItem tab = new DefaultCTabItem("SQL View");
		tab.activate();			
		return new DefaultStyledText().getText();
	}
	
	/**
	 * Selects column attribute
	 * @param index - if column is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0 
	 */
	public CriteriaBuilderDialog selectAttribute(String table, String attribute, int index) {
		log.info("Selecting attribute '"+ attribute + "' in table '" + table + "' (index=" + index + ")");
        new RadioButton(index, new WithMnemonicTextMatcher(RadioButtonType.COLUMN)).click();
		DefaultTree tree = new DefaultTree(index + 1);
		tree.setFocus();
		new DefaultTreeItem(tree, table, table + "." + attribute).select();
		return this;
	}
	
	/** 
	 * Opens function's expression builder
	 * @param index - if function is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public ExpressionBuilderDialog openFunctionBuilder(int index) {
		log.info("Opening function's expresion builder (index=" + index + ")");
        new RadioButton(index, new WithMnemonicTextMatcher(RadioButtonType.FUNCTION)).click();
        new PushButton(index, new WithMnemonicTextMatcher("Edit...")).click();
		return new ExpressionBuilderDialog();	
	}
	
	/**
	 * Sets value in type combo box under constant editor
	 * @param index - if constant is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public CriteriaBuilderDialog selectConstantType(String type, int index){
		log.info("Setting constant type to '" + type + "' (index=" + index + ")");
        new RadioButton(index, new WithMnemonicTextMatcher(RadioButtonType.CONSTANT)).click();
		new DefaultCombo(index,new WithLabelMatcher("Type:")).setSelection(type);
		return this;
	}
	
	/**
	 * Sets value in value text field under constant editor
	 * @param index - if constant is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public CriteriaBuilderDialog selectConstantValue(String value, int index){
		log.info("Setting constant value to '" + value + "' (index=" + index + ")");
        new RadioButton(index, new WithMnemonicTextMatcher(RadioButtonType.CONSTANT)).click();
        DefaultGroup groupFromSide = new DefaultGroup(index, new WithMnemonicTextMatcher("Value"));
		new DefaultText(groupFromSide).setText(value);
		return this;
	}
}
