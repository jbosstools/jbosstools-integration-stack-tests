package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class InterfacesTab {

	/**
	 * 
	 * @param name
	 * @param implementation
	 * @param operationList
	 */
	public void addInterface(String name, String implementation, String [] operationList) {
		new SectionToolItem("Interface List", "Add").click();
		
		new LabeledText("Name").setText(name);
		new LabeledText("Implementation").setText(implementation);

//		// Add operations
		
	}
	
	/**
	 * 
	 * @param name
	 * @param implementation
	 * @param operationList
	 */
	public void importInterface(String name, String implementation, String ... operationList) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void deleteInterface(String name) {
		DefaultSection s = new DefaultSection("Interface List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Interface List", "Remove").click();
	}
}
