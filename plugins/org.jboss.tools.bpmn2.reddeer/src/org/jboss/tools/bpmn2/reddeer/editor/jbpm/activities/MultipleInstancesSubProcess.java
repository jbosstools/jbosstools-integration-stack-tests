package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ComboSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.LabeledTextSetUpCTab;

public class MultipleInstancesSubProcess extends ElementContainer {

	public MultipleInstancesSubProcess(String name) {
		super(name, ElementType.MULTIPLE_SUB_PROCESS);
	}
	
	public void setInputCollection(String collectionName, String collectionDataType) {
		graphitiProperties.setUpTabs(new ComboSetUpCTab("Sub Process", "Input Data Collection",collectionName + " (" + collectionDataType + ")"));
	}
	
	public void setIteratorTroughCollection(String iteratorName) {
		graphitiProperties.setUpTabs(new LabeledTextSetUpCTab("Sub Process", "Input Instance Parameter", iteratorName));
	}
}
