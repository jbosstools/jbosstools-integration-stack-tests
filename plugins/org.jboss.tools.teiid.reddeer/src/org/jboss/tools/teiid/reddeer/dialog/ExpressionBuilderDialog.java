package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;

public class ExpressionBuilderDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(ExpressionBuilderDialog.class);
	
	public static class RadioButtonType {
		public static final String COLUMN = "Show the Column Editor";
		public static final String CONSTANT = "Show the Constant Editor";
		public static final String FUNCTION = "Show the Function Editor";
	}

	public ExpressionBuilderDialog(){
		super("Expression Builder");
	}
	
	@Override
	public void activate(){
		super.activate();
		new DefaultCTabItem("Tree View").activate();
	}
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
	}

	/**
	 * Select item in Tree View
	 */
	public ExpressionBuilderDialog selectTreeViewItem(String... itemPath) {
		log.info("Selecting item " + Arrays.toString(itemPath) + " in 'Tree View'");
		new DefaultTreeItem(itemPath).select();
		return this;
	}
	
	/**
	 * Selects specified radio button.
	 * @param type - ExpressionBuilder.RadioButtonType.COLUMN|CONSTANT|FUNCTION
	 */
	public ExpressionBuilderDialog selectRadioButton(String type) {
		log.info("Selecting " + type + " radio button");
		new RadioButton(new ButtonWithToolTipMatcher(type)).click();
		return this;
	}
	
	/**
	 * Selects column attribute
	 */
	public ExpressionBuilderDialog selectColumnAttribute(String table, String attribute) {
		log.info("Selecting attribute '"+ attribute + "' in table '" + table + "'");
		DefaultTree tree = new DefaultTree(1);
		tree.setFocus();
		new DefaultTreeItem(tree, table, table + "." + attribute).select();
		return this;
	}
	
	/**
	 * Sets value in type combo box under constant editor
	 */
	public ExpressionBuilderDialog selectConstantType(String type){
		log.info("Setting constant type to '" + type + "'");
		new LabeledCombo("Type:").setSelection(type);
		return this;
	}
	
	/**
	 * Sets value in value text field under constant editor
	 */
	public ExpressionBuilderDialog selectConstantValue(String value){
		log.info("Setting constant value to '" + value + "'");
		new DefaultText(new DefaultGroup("Value")).setText(value);
		return this;
	}
	
	/**
	 * Sets value in category combo box under function editor
	 */
	public ExpressionBuilderDialog selectFunctionCategory(String category){
		log.info("Setting fucntion category to '" + category + "'");
		new LabeledCombo("Category:").setSelection(category);
		return this;
	}
	
	/**
	 * Sets value in function combo box under function editor
	 */
	public ExpressionBuilderDialog selectFunctionValue(String value){
		log.info("Setting fucntion value to '" + value + "'");
		new LabeledCombo("Function:").setSelection(value);
		return this;
	}
	
	/**
	 * Applies built criteria.
	 */
	public ExpressionBuilderDialog apply() {
		log.info("Applying built criteria");
		new PushButton("Apply").click();
		return this;
	}
}
