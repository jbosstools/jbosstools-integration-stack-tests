package org.jboss.tools.teiid.reddeer.matcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Decides whether item of model editor is attribute with given name and from given parent item. 
 */
public class ModelEditorItemAttributeMatcher extends BaseMatcher<EditPart> {
	private String name;
	private String parentType;
	private String parentName;
	private List<String> allNames;
	
	/** 
	 * @param namePrefix - prefix of name of the attribute
	 * @param parentType ModelEditor.ItemType.*
	 * @param parentName - name of the parent
	 */
	public ModelEditorItemAttributeMatcher(String name, String parentType, String parentName){
		this.name = name;
		this.parentType = parentType;
		this.parentName = parentName;
		allNames = new ArrayList<String>();
	}	

	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart) {			
			if (item.getClass().toString().equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlAttributeEditPart")) {
				for (Object o : ((GraphicalEditPart) item).getFigure().getChildren()) {
					if (new ModelEditorItemMatcher(parentType, parentName).matches(((GraphicalEditPart) item).getParent().getParent())){
						if (o instanceof Label){
							Label label = (Label) o;
							allNames.add(label.getText());
							if (label.getText().startsWith(name)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is an attribute with name " + name + " of parent " + parentName);
	}

	public List<String> getAllAttributeNames() {
		return allNames;
	}
}
