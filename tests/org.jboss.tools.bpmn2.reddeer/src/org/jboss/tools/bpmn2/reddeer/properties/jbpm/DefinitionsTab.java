package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImportJavaTypeDialog;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class DefinitionsTab {

	/**
	 * 
	 * @param name
	 */
	public void addImport(String name) {
		new DefaultSection("Imports").getToolbarButton("Add").click();
		new ImportJavaTypeDialog().add(name);
	}

	/**
	 * 
	 * @param name
	 */
	public void removeImport(String name) {
		DefaultSection s = new DefaultSection("Imports");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		new DefaultSection("Data Type List").getToolbarButton("Add").click();
		new LabeledText("Structure").setText(name);
		new DefaultSection("Data Type Details").getToolbarButton("Close").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void removeDataType(String name) {
		DefaultSection s = new DefaultSection("Data Type List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		new DefaultSection("Message List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		new PushButton(0).click();
		new DataTypeDialog().add(dataType);
		new DefaultSection("Message Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeMessage(String name) {
		DefaultSection s = new DefaultSection("Message List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}

	/**
	 * 
	 * @param name
	 * @param errorCode
	 * @param dataType
	 */
	public void addError(String name, String errorCode, String dataType) {
		new DefaultSection("Error List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		new LabeledText("Error Code").setText(errorCode);
		new PushButton(0).click();
		new DataTypeDialog().add(dataType);
		new DefaultSection("Error Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeError(String name) {
		DefaultSection s = new DefaultSection("Error List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void addSignal(String name) {
		new DefaultSection("Signal List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		new DefaultSection("Signal Details").getToolbarButton("Close").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void removeSignal(String name) {
		DefaultSection s = new DefaultSection("Signal List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param escalationCode
	 */
	public void addEscalation(String name, String escalationCode) {
		new DefaultSection("Escalation List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		new LabeledText("Escalation Code").setText(escalationCode);
		new DefaultSection("Escalation Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeEscalation(String name) {
		DefaultSection s = new DefaultSection("Escalation List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}

}
