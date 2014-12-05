package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class DataItemsTab {

	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param local
	 */
	public void addVariable(String name, String dataType, boolean local, String processName) {
 		
		new SectionToolItem(
				(local ? "Local" : "Global") + " Variable List for Process \"" + processName + "\"",
				"Add").click();
 		new LabeledText(new DefaultSection((local ? "Local" : "Global") + " Variable Details"), "Name").setText(name); 		
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new SectionToolItem((local ? "Local" : "Global") + " Variable Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param local
	 */
	public void removeVariable(String name, boolean local, String processName) {
		DefaultSection s = new DefaultSection((local ? "Local" : "Global") + " List for Process \"" + processName + "\"");
		new DefaultTable(s).select(name);
		new SectionToolItem((local ? "Local" : "Global") + " List for Process \"" + processName + "\"", "Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addGlobalVariable(String name, String dataType, String processName) {
		addVariable(name, dataType, false, processName);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeGlobalVariable(String name, String processName) {
		removeVariable(name, false, processName);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType, String processName) {
		addVariable(name, dataType, true, processName);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeLocalVariable(String name, String processName) {
		removeVariable(name, true, processName);
	}
	
}
