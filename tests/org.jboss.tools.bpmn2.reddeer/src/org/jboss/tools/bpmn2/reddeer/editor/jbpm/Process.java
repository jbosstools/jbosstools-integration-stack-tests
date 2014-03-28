package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DataItemsTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DefinitionsTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.InterfacesTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ProcessTab;

/**
 * 
 */
public class Process extends org.jboss.tools.bpmn2.reddeer.editor.AbstractProcess {

	/**
	 * 
	 * @param name
	 */
	public Process(String name) {
		super(name);
		select();
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		properties.getTab("Process", ProcessTab.class).setId(id);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		properties.getTab("Process", ProcessTab.class).setName(name);
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		properties.getTab("Process", ProcessTab.class).setVersion(version);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setPackageName(String name) {
		properties.getTab("Process", ProcessTab.class).setPackageName(name);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setAddHoc(boolean value) {
		properties.getTab("Process", ProcessTab.class).setAdHoc(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setExecutable(boolean value) {
		properties.getTab("Process", ProcessTab.class).setExecutable(value);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		properties.getTab("Definitions", DefinitionsTab.class).addDataType(name);
	}
	
	/**
	 * 
	 * @param dataType
	 */
	public void addImport(String dataType) {
		properties.getTab("Definitions", DefinitionsTab.class).addImport(dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		properties.getTab("Definitions", DefinitionsTab.class).addMessage(name, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public void addError(String name, String code, String dataType) {
		properties.getTab("Definitions", DefinitionsTab.class).addError(name, code, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param code
	 */
	public void addEscalation(String name, String code) {
		properties.getTab("Definitions", DefinitionsTab.class).addEscalation(name, code);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addSignal(String name) {
		properties.getTab("Definitions", DefinitionsTab.class).addSignal(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addGlobalVariable(String name, String dataType) {
		properties.getTab("Data Items", DataItemsTab.class).addGlobalVariable(name, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addLocalVariable(String name, String dataType) {
		properties.getTab("Data Items", DataItemsTab.class).addLocalVariable(name, dataType);
	}
	
	/**
	 * 
	 * @param name
	 * @param implementation
	 * @param operationList
	 */
	public void addInterface(String name, String implementation, String ... operationList) {
		properties.getTab("Interfaces", InterfacesTab.class).addInterface(name, implementation, operationList);
	}

}
