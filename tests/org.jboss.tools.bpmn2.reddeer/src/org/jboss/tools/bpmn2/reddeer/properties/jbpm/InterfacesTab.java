package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.reddeer.DefaultSection;

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
//		DefaultSection s = new DefaultSection("Interface List");
//		s.getToolbarButton("Add").click();
//		
//		new LabeledText("Name").setText(name);
//		new LabeledText("Implementation").setText(implementation);
//		
//		// Add operations
		
		throw new UnsupportedOperationException();
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
		s.getTable().select(name);
		s.getToolbarButton("Delete").click();
	}
}
