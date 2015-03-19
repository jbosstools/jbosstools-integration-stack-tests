package org.jboss.tools.bpmn2.reddeer;

import org.eclipse.gef.EditPart;
import org.jboss.tools.bpmn2.reddeer.editor.graphiti.PropertiesGraphitiEditPart;
import org.jboss.tools.bpmn2.reddeer.properties.setup.SetUpAble;

public class PropertiesHandler {
	
	private ProcessPropertiesView propertiesView;
	private PropertiesGraphitiEditPart graphitiProperties;
	private boolean useGraphitiProperties;
	
	public PropertiesHandler(EditPart editPart, boolean useGraphitiProperties) {
		propertiesView = new ProcessPropertiesView(new AbsoluteEditPart(editPart));
		graphitiProperties = new PropertiesGraphitiEditPart(editPart);
		this.useGraphitiProperties = useGraphitiProperties;
	}
	
	public void setUp(SetUpAble... properties){
		if(useGraphitiProperties) {
			graphitiProperties.setUpTabs(properties);
		} else {
			for(SetUpAble property : properties) {
				propertiesView.selectTab(property.getTabLabel());
				property.setUpCTab();
			}
			
		}
	}
	
	public void setUpNormal(SetUpAble setUpAble){
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
		propertiesView.selectTab(tabLabel);
	}
	
	public String getTitleOfPropertiesView() {
		return propertiesView.getTitle();
	}
}
