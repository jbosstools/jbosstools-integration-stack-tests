package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway.Direction;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class GatewayTab {

	/**
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		new LabeledCombo("Gateway Direction").setSelection(direction.label());
	}
	
	/**
	 * 
	 * @param flow
	 * @param lang
	 * @param condition
	 */
	protected void setCondition(String flow, String lang, String condition) {
		new DefaultTable(0).select(flow);
		new DefaultSection("Sequence Flow List").getToolbarButton("Edit").click();
		new PushButton("Add Condition").click();
		new LabeledCombo("Condition Language").setSelection(lang);
		new LabeledText("Constraint").setText(condition);
		new DefaultSection("Sequence Flow Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param flow
	 */
	protected void setDefaultBranch(String flow) {
		new DefaultTable(0).select(flow);
		new DefaultSection("Sequence Flow List").getToolbarButton("Edit").click();
		new DefaultCheckBox().setChecked(true);
		new DefaultSection("Sequence Flow Details").getToolbarButton("Close").click();
	}

	/**
	 * 
	 * @param flow
	 * @param priority
	 */
	protected void setPriority(String flow, String priority) {
		new DefaultTable(0).select(flow);
		new DefaultSection("Sequence Flow List").getToolbarButton("Edit").click();
		new LabeledText("Priority").setText(priority);
		new DefaultSection("Sequence Flow Details").getToolbarButton("Close").click();
	}
}
