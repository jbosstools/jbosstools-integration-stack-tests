package org.jboss.tools.teiid.reddeer.editor;

import java.util.List;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;

@Deprecated // use %ModelEditor methods
public class ModelDiagram {

	protected SWTBotGefEditPart editPart;

	public ModelDiagram(SWTBotGefEditPart editPart) {
		if (editPart.getClass().toString().equals("")) {

		}
		this.editPart = editPart;
	}

	public SWTBotGefEditPart getEditPart() {
		return editPart;
	}

	public void select() {
		editPart.select();
	}

	public List<String> getModelAttributes() {		
		AttributeMatcher matcher = new AttributeMatcher("");
		for (SWTBotGefEditPart ch : editPart.children().get(0).children()){
			matcher.matches(ch.part());
		} 
		return matcher.getTexts();
	}

}
