package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class DataItemsTab {

	private String processName;

	/**
	 * 
	 */
	public DataItemsTab() {
		this.processName = new ProcessEditorView().getProcess().getName();
	}
	
	/**
	 * BZ-1089464 ('Local' is renamed to 'Variable')
	 * 
	 * @param name
	 * @param dataType
	 * @param local
	 */
	public void addVariable(String name, String dataType, boolean local) {
 		new DefaultSection((local ? "Local" : "Global") + " Variable List for Process \"" + processName + "\"").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new DefaultSection((local ? "Local" : "Global") + " Variable Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param local
	 */
	public void removeVariable(String name, boolean local) {
		DefaultSection s = new DefaultSection((local ? "Local" : "Global") + " List for Process \"" + processName + "\"");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addGlobalVariable(String name, String dataType) {
		addVariable(name, dataType, false);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeGlobalVariable(String name) {
		removeVariable(name, false);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		addVariable(name, dataType, true);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeLocalVariable(String name) {
		removeVariable(name, true);
	}
	
}
