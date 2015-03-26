package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DataItemsTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DefinitionsTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.InterfacesTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ProcessTab;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.LabeledTextSetUp;

/**
 * 
 */
public class Process extends ElementContainer {

	private static final String PROCESS_TAB = "Process";
	private static final String DEFINITIONS_TAB = "Definitions";
	private static final String DATA_ITEMS_TAB = "Data Items";
	private static final String INTERFACES_TAB = "Interfaces";
	private ProcessTab processTab;
	private DefinitionsTab definitionsTab;
	private DataItemsTab dataItemsTab;
	private InterfacesTab interfacesTab;
	/**
	 * 
	 * @param name
	 */
	public Process(String name) {
		super(name, ElementType.PROCESS);
		processTab = new ProcessTab();
		definitionsTab = new DefinitionsTab();
		dataItemsTab = new DataItemsTab();
		interfacesTab = new InterfacesTab();
	}
	
	/**
	 * Constructs currently opened process
	 */
	public Process() {
		this(null);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		propertiesHandler.setUpNormal(new LabeledTextSetUp(PROCESS_TAB, "Id", id));
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		propertiesHandler.setUpNormal(new LabeledTextSetUp(PROCESS_TAB, "Name", name));
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		propertiesHandler.setUpNormal(new LabeledTextSetUp(PROCESS_TAB, "Version", version));
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setPackageName(String packageName) {
		propertiesHandler.setUpNormal(new LabeledTextSetUp(PROCESS_TAB, "Package Name", packageName));
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setAddHoc(boolean value) {
		propertiesHandler.setUpNormal(new CheckBoxSetUp(PROCESS_TAB, "Ad Hoc", value));
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setExecutable(boolean value) {
		propertiesHandler.selectTabInPropertiesView(PROCESS_TAB);
		processTab.setExecutable(value);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addDataType(name);
	}
	
	/**
	 * 
	 * @param dataType
	 */
	public void addImport(String dataType) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addImport(dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addMessage(name, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public void addError(String name, String code, String dataType) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addError(name, code, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param code
	 */
	public void addEscalation(String name, String code) {
		Escalation escalation = new Escalation(name, code);
		addEscalation(escalation);
	}
	
	public void addEscalation(Escalation escalation) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addEscalation(escalation);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addSignal(String name) {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		definitionsTab.addSignal(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addGlobalVariable(String name, String dataType) {
		propertiesHandler.selectTabInPropertiesView(DATA_ITEMS_TAB);
		dataItemsTab.addGlobalVariable(name, dataType, this.name);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		addLocalVariable(name, dataType, false);
	}
	
	public void addLocalVariable(String name, String dataType, boolean typeText) {
		propertiesHandler.selectTabInPropertiesView(DATA_ITEMS_TAB);
		dataItemsTab.addLocalVariable(name, dataType, this.name, typeText);
	}
	
	/**
	 * 
	 * @param name
	 * @param implementation
	 * @param operationList
	 */
	public void addInterface(String name, String implementation, String ... operationList) {
		addInterface(name, implementation, false, operationList);
	}
	
	public void addInterface(String name, String implementation, boolean typeText, String ... operationList) {
		propertiesHandler.selectTabInPropertiesView(INTERFACES_TAB);
		interfacesTab.addInterface(name, implementation, operationList, typeText);
	}
	
	public void importInterface(String fullQualifiedName){
		propertiesHandler.selectTabInPropertiesView(INTERFACES_TAB);
		interfacesTab.importInterface(fullQualifiedName);
	}

	public String getFirstLocalVariable() {
		propertiesHandler.selectTabInPropertiesView(DATA_ITEMS_TAB);
		Table table = new DefaultTable(1);
		return table.getItem(0).getText(0);
	}
	
	public List<Signal> getSignals(){
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		Table table = new DefaultTable(new DefaultSection("Signal List"), 0);
		List<Signal> signals = new ArrayList<Signal>();
		
		for(TableItem item : table.getItems()) {
			signals.add(new Signal(item.getText(0)));
		}
		
		return signals;
	}
	
	public List<Message> getMessages() {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		Table table = new DefaultTable(new DefaultSection("Message List"), 0);
		
		List<Message> messages = new ArrayList<Message>();
		for(TableItem item : table.getItems()) {
			Message message = new Message(item.getText(0), item.getText(1));
			messages.add(message);
		}
		
		return messages;
	}
	
	public List<Escalation> getEscalations() {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		Table table = new DefaultTable(new DefaultSection("Escalation List"), 0);
		List<Escalation> escalations = new ArrayList<Escalation>();
		
		for(TableItem item : table.getItems()) {
			Escalation escalation = new Escalation(item.getText(0), item.getText(1));
			escalations.add(escalation);
		}
		
		return escalations;
	}
	
	public List<ErrorRef> getErrors() {
		propertiesHandler.selectTabInPropertiesView(DEFINITIONS_TAB);
		Table table = new DefaultTable(new DefaultSection("Error List"), 0);
		List<ErrorRef> errors = new ArrayList<ErrorRef>();
		
		for(TableItem item : table.getItems()) {
			ErrorRef error = new ErrorRef(item.getText(0), item.getText(1), item.getText(2));
			errors.add(error);
		}
		
		return errors;
	}
}
