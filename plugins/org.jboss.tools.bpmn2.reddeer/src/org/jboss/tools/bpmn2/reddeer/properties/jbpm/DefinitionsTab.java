package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImportJavaTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class DefinitionsTab {

	/**
	 * 
	 * @param name
	 */
	public void addImport(String name) {
		new SectionToolItem("Imports", "Add").click();
		new ImportJavaTypeDialog().add(name);
	}

	/**
	 * 
	 * @param name
	 */
	public void removeImport(String name) {
		DefaultSection s = new DefaultSection("Imports");
		new DefaultTable(s).select(name);
		new SectionToolItem("Imports", "Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		new SectionToolItem("Data Type List", "Add").click();
		new LabeledText("Structure").setText(name);
		new SectionToolItem("Data Type Details", "Close").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void removeDataType(String name) {
		DefaultSection s = new DefaultSection("Data Type List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Data Type Details", "Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		
		new SectionToolItem("Message List", "Add").click();
		new Message(name, dataType).setUp("Message Details");
		new SectionToolItem("Message Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeMessage(String name) {
		DefaultSection s = new DefaultSection("Message List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Message List", "Remove").click();
	}

	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public void addError(String name, String code, String dataType) {
		new SectionToolItem("Error List", "Add").click();
		new ErrorRef(name, code, dataType).setUp();
		new SectionToolItem("Error Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeError(String name) {
		DefaultSection s = new DefaultSection("Error List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Error List", "Remove").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void addSignal(String name) {
		new SectionToolItem("Signal List", "Add").click();
		new Signal(name).setUp();
		new SectionToolItem("Signal Details", "Close").click();
	}

	/**
	 * 
	 * @param name
	 */
	public void removeSignal(String name) {
		DefaultSection s = new DefaultSection("Signal List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Signal List", "Remove").click();
	}
	
	/**
	 * 
	 * @param name
	 * @param escalationCode
	 */
	public void addEscalation(String name, String code) {
		Escalation escalation = new Escalation(name, code);
		addEscalation(escalation);
	}
	
	public void addEscalation(Escalation escalation) {
		new SectionToolItem("Escalation List", "Add").click();
		escalation.setUpViaProcessDefinitions();
		new SectionToolItem("Escalation Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeEscalation(String name) {
		DefaultSection s = new DefaultSection("Escalation List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Escalation List", "Remove").click();
	}

}
