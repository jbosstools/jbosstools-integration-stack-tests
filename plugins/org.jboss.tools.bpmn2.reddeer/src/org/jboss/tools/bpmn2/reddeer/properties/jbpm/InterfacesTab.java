package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.reddeer.DefaultCheckBox;
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
	 * @param interfaceName - example "String"
	 */
	public void importWholeInterface(String interfaceName) {
		DefaultSection s = new DefaultSection("Interface List");
		s.getToolbarButton("Import").click();
		
		new DefaultShell("Browse for a Java type to Import").setFocus();
		new LabeledText("Type:").typeText(interfaceName);
		
		new DefaultTree().selectItems(new DefaultTreeItem(interfaceName));
		
		new PushButton("Select All").click();
		new DefaultCheckBox("Create Process Variables").click();
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
