package org.jboss.tools.bpmn2.reddeer.editor.switchyard.activities;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.OperationDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItemButton;
import org.jboss.tools.bpmn2.reddeer.properties.setup.LabeledTextSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ParameterMappingSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

/**
 * TODO: apodhrad
 */
public class SwitchYardServiceTask extends Element {

	@SuppressWarnings("unused")
	private static final int ON_ENTRY = 0;
	@SuppressWarnings("unused")
	private static final int ON_EXIT = 1;
	
	public SwitchYardServiceTask(String name) {
		super(name, ElementType.SWITCHYARD_SERVICE_TASK);
	}
	
	public void setOperation(String name) {
		propertiesHandler.selectTabInPropertiesView("Service Task");
		 
		new PushButton(0).click();
		new OperationDialog().addOperation(name, null, null, null);
	}
	
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new ParameterMappingSetUp(parameterMapping, SectionToolItemButton.ADD));
	}
	
	public void setTaskAttribute(String attribute, String text){
		propertiesHandler.setUpNormal(new LabeledTextSetUp(PropertiesTabs.SWITCHYARD_TAB, attribute, text));
	}
	
	/**
	 * 
	 * @param scriptLang
	 * @param text
	 * @param type ON_ENTRY|ON_EXIT
	 */
	public void setScript(String scriptLang, String text, int type){
		String section = null;
		switch (type) {
			case ON_ENTRY:
				section = "On Entry Script";
				break;
			case ON_EXIT:
				section = "On Exit Script";
		}
		propertiesHandler.setUpNormal(new ScriptSetUp(PropertiesTabs.SWITCHYARD_TAB, section, scriptLang, text));
	}

}
