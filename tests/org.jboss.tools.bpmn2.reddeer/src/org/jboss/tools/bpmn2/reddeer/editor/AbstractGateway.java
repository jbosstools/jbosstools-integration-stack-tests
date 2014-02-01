package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;


/**
 * 	
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractGateway extends Construct {

	/**
	 * 
	 */
	public enum Direction {
		CONVERGING, DIVERGING, MIXED, UNSPECIFIED
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractGateway(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param direction
	 */
	protected void setDirection(Direction direction) {
		select();
		String visibleText = direction.name().charAt(0) + direction.name().substring(1).toLowerCase();
		properties.selectTab("Gateway");
		new DefaultCombo("Gateway Direction").setSelection(visibleText);
	}
	
	/**
	 * 
	 * @param branch
	 * @param lang
	 * @param condition
	 */
	protected void setCondition(String branch, String lang, String condition) {
		select();
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch);
		properties.toolbarButton("Sequence Flow List", "Edit").click();
		new PushButton("Add Condition").click();
		new DefaultCombo("Condition Language").setSelection(lang);
		new LabeledText("Constraint").setText(condition);
		properties.toolbarButton("Sequence Flow Details", "Close").click();
	}
	
	/**
	 * 
	 * @param branch
	 */
	protected void setDefaultBranch(String branch) {
		select();
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch);
		bot.toolbarButtonWithTooltip("Edit").click();
		new CheckBox().click();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
