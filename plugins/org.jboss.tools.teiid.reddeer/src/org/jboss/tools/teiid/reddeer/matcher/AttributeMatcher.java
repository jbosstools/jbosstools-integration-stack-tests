package org.jboss.tools.teiid.reddeer.matcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

/**
 * Decides whether item of model editor is attribute with given name. 
 */
public class AttributeMatcher extends BaseMatcher<EditPart> {
	private String prefix;
	private String parentType;
	private String parentPrefix;
	private List<String> texts = new ArrayList<String>();
	
	/** 
	 * @param prefix - prefix of name or full name of the attribute
	 */
	public AttributeMatcher(String prefix){
		this.prefix = prefix;
		parentType = null;
		parentPrefix = null;
	}	
	
	/** 
	 * will match only attributes of given parent type, prefix
	 * @param prefix - prefix of name or full name of the attribute
	 * @param parentType MAPPING_CLASS|TABLE|...
	 * @param parentPrefix - prefix of name or full name of the item
	 */
	public AttributeMatcher(String prefix, String parentType, String parentPrefix){
		this.prefix = prefix;
		this.parentType = parentType;
		this.parentPrefix = parentPrefix;
	}	

	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart) {			
			if (item.getClass().toString().equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlAttributeEditPart")) {
				for (Object o : ((GraphicalEditPart) item).getFigure().getChildren()) {
					if ((o instanceof Label) && (((Label) o).getText().startsWith(this.prefix))) {
						if (parentType == null){
							texts.add(((Label) o).getText());
							return true;
						} else {
							if (new ModelEditorItemMatcher(parentType, parentPrefix).matches(((GraphicalEditPart) item).getParent().getParent())){
								texts.add(((Label) o).getText());
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
		description.appendText("is an attribute starting with given prefix");
	}

	public List<String> getTexts() {
		return texts;
	}
}
