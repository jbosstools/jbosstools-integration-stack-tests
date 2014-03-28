package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 *
 */
public class SubProcessTab {

	/**
	 * 
	 */
	public enum LoopCharacteristics {

		NONE("None"), MULTI_INSTANCE("Multi-Instance");
		
		private String label; 

		/**
		 * 
		 * @param label
		 */
		private LoopCharacteristics(String label) {
			this.label = label;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getLabel() {
			return label;
		}
		
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		new DefaultCheckBox("Is For Compensation").setChecked(value);
	}
	
	/**
	 * 
	 * @param lch
	 */
	public void setLoopCharacteristics(LoopCharacteristics lch) {
		new RadioButton(lch.getLabel()).click();
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addVariable(String name, String dataType) {
		new DefaultSection("Variable List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new DefaultSection("Variable Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeVariable(String name) {
		DefaultSection s = new DefaultSection("Variable List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setOnEntryScript(Expression expression) {
		DefaultSection s = new DefaultSection("On Entry Script");
		s.getComboBox("Script Language").setSelection(expression.getLanguage());
		s.getText("Script").setText(expression.getScript());
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setOnExitScript(Expression expression) {
		DefaultSection s = new DefaultSection("On Exit Script");
		s.getComboBox("Script Language").setSelection(expression.getLanguage());
		s.getText("Script").setText(expression.getScript());
	}
}
