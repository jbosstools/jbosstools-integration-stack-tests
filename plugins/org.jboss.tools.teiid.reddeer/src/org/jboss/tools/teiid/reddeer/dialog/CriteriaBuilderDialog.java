package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.eclipse.swt.widgets.Button;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.matcher.WithLabelMatcher;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;

public class CriteriaBuilderDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(CriteriaBuilderDialog.class);
	
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
	
	public CriteriaBuilderDialog() {
		super("Criteria Builder");
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
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
		new RadioButton(leftRight,new ButtonWithToolTipMatcher(type)).click();
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
		new PushButton(index,new BaseMatcher<Button>(){
			@Override
			public boolean matches(Object o) {
				return (o instanceof Button) && ((Button) o).getText().equals("Edit...");
			}
			@Override
			public void describeTo(Description arg0) {}	
		}).click();
		return new ExpressionBuilderDialog();	
	}
	
	/**
	 * Sets value in type combo box under constant editor
	 * @param index - if constant is selected on both sides, use CriteriaBuilder.CriteriaSide.LEFT|RIGHT
	 * 				- if only one side, use 0
	 */
	public CriteriaBuilderDialog selectConstantType(String type, int index){
		log.info("Setting constant type to '" + type + "' (index=" + index + ")");
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
		new DefaultText(new DefaultGroup("Value"),index).setText(value);
		return this;
	}
}
