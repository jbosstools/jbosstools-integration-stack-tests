package org.jboss.tools.bpmn2.reddeer;

import static org.hamcrest.Matchers.allOf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOfType;
import org.jboss.tools.reddeer.finder.AllEditPartFinder;
import org.jboss.tools.reddeer.matcher.EditPartOfClassName;

public class GEFProcessEditor extends GEFEditor {
	
	public List<EditPart> getAllEditParts(ElementType type) {
		EditPart parent = viewer.getContents();
		
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
		matcherList.add(new ConstructOfType<EditPart>(type));
		
		AllEditPartFinder finder = new AllEditPartFinder();
		return finder.find(parent, allOf(matcherList));
	}
	

}
