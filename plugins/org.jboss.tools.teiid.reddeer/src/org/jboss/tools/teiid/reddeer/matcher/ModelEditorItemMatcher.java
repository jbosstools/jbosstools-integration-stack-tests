package org.jboss.tools.teiid.reddeer.matcher;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Decides whether item of model editor is of given type and name. 
 */
public class ModelEditorItemMatcher extends BaseMatcher<EditPart> {
	public static final String MAPPING_CLASS = "<<Mapping Class>>";
	public static final String TABLE = "<<Table>>";
	public static final String XML_DOCUMENT = "<<XML Document>>";
	public static final String INPUT_SET = "<<Input Set>>";
	public static final String STAGING_TABLE = "<<Staging Table>>";
	public static final String INTERFACE = "<<Interface>>";
	
	private String type;
	private String prefix;
	
	/** 
	 * @param type ModelEditorItemMatcher.MAPPING_CLASS|TABLE|...
	 * @param prefix - prefix of name or full name of the item
	 */
	public ModelEditorItemMatcher(String type, String prefix){
		this.type = type;
		this.prefix = prefix;
	}
	
	@Override
	public boolean matches(Object item) {
		boolean isGivenType = false;
		boolean startsWithPrefix = false;
		if (item instanceof GraphicalEditPart) {
			String itemClass = item.getClass().toString();
			if (itemClass.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlClassifierEditPart")
					|| itemClass.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlPackageEditPart")) {
				for (IFigure itemChild : new ArrayList<IFigure>(((GraphicalEditPart) item).getFigure().getChildren())) {
					String itemChildClass = itemChild.getClass().toString();
					if (itemChildClass.equals("class org.teiid.designer.diagram.ui.notation.uml.figure.UmlClassifierHeader")
							|| itemChildClass.equals("class org.teiid.designer.diagram.ui.figure.LabeledRectangleFigure")) {
						for (IFigure headerChild : new ArrayList<IFigure>(itemChild.getChildren())) {
							if (headerChild instanceof Label) {
								String text = ((Label) headerChild).getText();
								if (text.equals(type)) {
									isGivenType = true;
								}
								if (text.startsWith(prefix)) {
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
		description.appendText(" is a " + type + " with prefix " + prefix);
	}
}
