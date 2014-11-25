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
	
	private AllEditPartFinder finder = new AllEditPartFinder();
	
	public List<EditPart> getAllEditParts(ElementType type) {
		EditPart parent = viewer.getContents();
		
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
		matcherList.add(new ConstructOfType<EditPart>(type));
		return finder.find(parent, allOf(matcherList));
	}
	
	public List<EditPart> getAllEditParts(EditPart parent, Matcher<org.eclipse.gef.EditPart> matcher) {
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
		matcherList.add(matcher);
		
		List<EditPart> result =  finder.find(parent, allOf(matcherList));
		return result;
	}
	
	/**
	 * Returns edit part which wraps all elements of process
	 * @return EditPart - which is instance of some internal DiagramEditPart
	 */
	public EditPart getRootEditPart() {
		return viewer.getContents();
	}

}
