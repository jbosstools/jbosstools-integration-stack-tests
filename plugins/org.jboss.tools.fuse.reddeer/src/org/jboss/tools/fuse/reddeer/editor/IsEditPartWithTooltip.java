package org.jboss.tools.fuse.reddeer.editor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.ui.platform.GraphitiShapeEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher which returns true if an edit part has a given tooltip.
 * 
 * @author apodhrad
 * 
 */
public class IsEditPartWithTooltip extends BaseMatcher<EditPart> {

	private String tooltip;

	public IsEditPartWithTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof GraphitiShapeEditPart) {
			IFigure figure = ((GraphitiShapeEditPart) obj).getFigure();
			IFigure tooltip = figure.getToolTip();
			if (tooltip instanceof Label) {
				Label label = (Label) tooltip;
				return label.getText().equals(this.tooltip);
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with tooltip '" + tooltip + "'");
	}

}