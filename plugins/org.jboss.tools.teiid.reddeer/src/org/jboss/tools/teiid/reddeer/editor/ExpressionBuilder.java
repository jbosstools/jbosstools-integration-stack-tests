package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swt.widgets.Button;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;

public class ExpressionBuilder {
	
	public static class RadioButtonType {
		public static final String COLUMN = "Show the Column Editor";
		public static final String CONSTANT = "Show the Constant Editor";
		public static final String FUNCTION = "Show the Function Editor";
	}

	/**
	 * opens e.b. from criteria builder
	 * @param index - CriteriaBuilder.CriteriaSide.LEFT|RIGHT or 0 
	 */
	public ExpressionBuilder(int index){
		new PushButton(index,new BaseMatcher<Button>(){
			@Override
			public boolean matches(Object o) {
				return (o instanceof Button) && ((Button) o).getText().equals("Edit...");
			}
			@Override
			public void describeTo(Description arg0) {}	
		}).click();
		activate();
	}
	
	/**
	 * opens e.b. from reconciler
	 * @param reconciler - w/e - used just to diff. constructors 
	 */
	public ExpressionBuilder(boolean reconciler){
		new PushButton(new ButtonWithToolTipMatcher("Create Expression")).click();
		activate();
	}
	
	/**
	 * default - e.b. must be opened before
	 */
	public ExpressionBuilder(){
		//new PushButton(new ButtonWithToolTipMatcher("Expression Builder")).click();
		activate();
	}
	
	public ExpressionBuilder activate(){
		new DefaultShell("Expression Builder");
		new DefaultCTabItem("Tree View").activate();
		return this;
	}
	
	/**
	 * select item in Tree View
	 * @param itemPath
	 */
	public void selectTreeViewItem(String... itemPath) {
		new DefaultTreeItem(itemPath).select();
	}
	
	/**
	 * @param type - ExpressionBuilder.RadioButtonType.COLUMN|CONSTANT|FUNCTION
	 */
	public void selectRadioButton(String type) {
		new RadioButton(new ButtonWithToolTipMatcher(type)).click();
	}
	
	/**
	 * select column attribute (for relational sources)
	 */
	public void selectColumnAttribute(String table, String attribute) {
		DefaultTree tree = new DefaultTree(1);
		tree.setFocus();
		new DefaultTreeItem(tree, table, table + "." + attribute).select();
	}
	
	/**
	 * set value in type combo box under constant editor
	 */
	public void selectConstantType(String type){
		new LabeledCombo("Type:").setSelection(type);
	}
	
	/**
	 * set value in value text field under constant editor
	 */
	public void selectConstantValue(String value){
		new DefaultText(new DefaultGroup("Value")).setText(value);
	}
	
	/**
	 * set value in category combo box under function editor
	 */
	public void selectFunctionCategory(String value){
		new LabeledCombo("Category:").setSelection(value);
	}
	
	/**
	 * set value in function combo box under function editor
	 */
	public void selectFunctionValue(String value){
		new LabeledCombo("Function:").setSelection(value);
	}
	
	/**
	 * apply criteria
	 */
	public void apply() {
		new PushButton("Apply").click();
	}
	
	/**
	 * finish expression builder
	 */
	public void finsih(){
		new PushButton("OK").click();
	}
}
