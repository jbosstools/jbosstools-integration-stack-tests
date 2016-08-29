package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Decides whether item of model editor is of given type and name prefix. 
 */
public class ModelEditorItemMatcher extends BaseMatcher<EditPart> {	
	private String type;
	private String namePrefix;
	
	/** 
	 * @param type - ModelEditor.ItemType.*
	 * @param name - name of the item
	 */
	public ModelEditorItemMatcher(String type, String namePrefix){
		this.type = type;
		this.namePrefix = namePrefix;
	}
	
	@Override
	public boolean matches(Object item) {
		boolean isGivenType = false;
		boolean startsWithPrefix = false;
		if (item instanceof GraphicalEditPart) {
			String itemClass = item.getClass().toString();
			if (itemClass.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlClassifierEditPart")
					|| itemClass.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlPackageEditPart")) {
				for (Object child : ((GraphicalEditPart) item).getFigure().getChildren()) {
					IFigure itemChild = (IFigure) child;
					String itemChildClass = itemChild.getClass().toString();
					if (itemChildClass.equals("class org.teiid.designer.diagram.ui.notation.uml.figure.UmlClassifierHeader")
							|| itemChildClass.equals("class org.teiid.designer.diagram.ui.figure.LabeledRectangleFigure")) {
						for (Object headerChild : itemChild.getChildren()) {
							if (headerChild instanceof Label) {
								String text = ((Label) headerChild).getText();
								if (text.equals(type)) {
									isGivenType = true;
								}
								if (text.startsWith(namePrefix)) {
									startsWithPrefix = true;
								}
							}
						}
					}
				}
			}	
		}
		return isGivenType && startsWithPrefix;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" is a " + type + " with name prefix " + namePrefix);
	}
}
