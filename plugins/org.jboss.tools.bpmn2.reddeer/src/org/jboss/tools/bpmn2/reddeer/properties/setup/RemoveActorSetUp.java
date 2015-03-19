package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class RemoveActorSetUp implements SetUpAble {

	private String tabName;
	private String actorName;
	
	public RemoveActorSetUp(String tabName, String actorName) {
		this.tabName = tabName;
		this.actorName = actorName;
	}
	
	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Actors");
		new DefaultTable(section).select(actorName);
		new SectionToolItem("Actors", "Remove").click();
		
	}

	@Override
	public String getTabLabel() {
		return tabName;
	}

}
