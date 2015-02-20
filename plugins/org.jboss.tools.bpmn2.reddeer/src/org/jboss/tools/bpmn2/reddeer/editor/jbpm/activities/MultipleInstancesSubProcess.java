package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ComboSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.LabeledTextSetUpCTab;

public class MultipleInstancesSubProcess extends ElementContainer {

	private static final String SUB_PROCESS = "Sub Process";

	public MultipleInstancesSubProcess(String name) {
		super(name, ElementType.MULTIPLE_SUB_PROCESS);
	}
	
	public void setInputCollection(String collectionName, String collectionDataType) {
		propertiesHandler.setUp(new ComboSetUpCTab(SUB_PROCESS, "Input Data Collection",collectionName + " (" + collectionDataType + ")"));
	}
	
	public void setIteratorTroughCollection(String iteratorName) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(SUB_PROCESS, "Input Instance Parameter", iteratorName));
	}
}
