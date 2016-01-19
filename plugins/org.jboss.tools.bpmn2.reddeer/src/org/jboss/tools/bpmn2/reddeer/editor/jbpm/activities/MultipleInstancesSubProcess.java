package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementContainer;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.properties.setup.MultipleInstanceCollectionSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.MultipleInstanceIteratorSetUp;

public class MultipleInstancesSubProcess extends ElementContainer {

	public MultipleInstancesSubProcess(String name) {
		super(name, ElementType.MULTIPLE_SUB_PROCESS);
	}

	public void setInputCollection(String collectionName) {
		propertiesHandler.setUp(new MultipleInstanceCollectionSetUp(PropertiesTabs.SUB_PROCESS_TAB, collectionName));
	}

	public void setIteratorTroughCollection(String iteratorName) {
		propertiesHandler.setUp(new MultipleInstanceIteratorSetUp(PropertiesTabs.SUB_PROCESS_TAB, iteratorName));
	}
}
