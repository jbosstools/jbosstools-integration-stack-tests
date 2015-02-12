package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
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
	
	public void importInterface(String fullQualifiedName){
		new SectionToolItem("Interface List", "Import").click();
		
		new DefaultShell("Browse for a Java type to Import").setFocus();
		new LabeledText("Type:").typeText(fullQualifiedName);
		
		String type = fullQualifiedName.substring(fullQualifiedName.lastIndexOf(".")+1);
		
		new DefaultTree(0).selectItems(new DefaultTreeItem(type + " - " + fullQualifiedName));
		
		new PushButton("Select All").click();
		new CheckBox(0).click();
		new PushButton("OK").click();
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
