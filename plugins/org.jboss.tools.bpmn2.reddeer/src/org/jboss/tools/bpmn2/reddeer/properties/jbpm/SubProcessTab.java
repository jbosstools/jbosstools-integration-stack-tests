package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;

/**
 *
 */
public class SubProcessTab extends GeneralPropertiesTab {

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
	public void addLocalVariable(String name, String dataType) {
		new SectionToolItem("Local Variable List", "Add").click();
		new LabeledText(new DefaultSection("Local Variable Details"), "Name").setText(name);
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new SectionToolItem("Local Variable Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeVariable(String name) {
		DefaultSection s = new DefaultSection("Local Variable List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Local Variable List", "Remove").click();
	}
	
}
