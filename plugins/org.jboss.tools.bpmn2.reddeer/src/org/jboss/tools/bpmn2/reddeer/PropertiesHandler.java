package org.jboss.tools.bpmn2.reddeer;

import org.eclipse.gef.EditPart;
import org.jboss.tools.bpmn2.reddeer.editor.graphiti.PropertiesGraphitiEditPart;
import org.jboss.tools.bpmn2.reddeer.properties.setup.SetUpAble;

public abstract class PropertiesHandler {

	private WaitingPropertiesView propertiesView;
	private PropertiesGraphitiEditPart graphitiProperties;
	private boolean useGraphitiProperties;

	public PropertiesHandler(EditPart editPart, boolean useGraphitiProperties) {
		propertiesView = new WaitingPropertiesView();
		graphitiProperties = new PropertiesGraphitiEditPart(editPart);
		this.useGraphitiProperties = useGraphitiProperties;

		propertiesView.open();
	}

	public void setUp(SetUpAble... properties) {
		if (useGraphitiProperties) {
			graphitiProperties.setUpTabs(properties);
		} else {
			for (SetUpAble property : properties) {
				focusElement();
				propertiesView.selectTab(property.getTabLabel());
				property.setUpCTab();
			}
		}
	}

	public void setUpNormal(SetUpAble setUpAble) {
		propertiesView.selectTab(setUpAble.getTabLabel());
		setUpAble.setUpCTab();
	}

	public void setUpGraphiti(SetUpAble setUpAble) {
		graphitiProperties.setUpTabs(setUpAble);
	}

	public void activatePropertiesView() {
		propertiesView.activate();
	}

	public void activateGraphitiPropertiesView() {
		graphitiProperties.getContextButton("Show Properties").click();
	}

	public void selectTabInPropertiesView(String tabLabel) {
		focusElement();
		propertiesView.selectTab(tabLabel);
	}

	public String getTitleOfPropertiesView() {
		return propertiesView.getTitle();
	}

	public abstract void focusElement();
}
