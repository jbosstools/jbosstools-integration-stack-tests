package org.jboss.tools.fuse.reddeer.editor.matcher;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.tools.fuse.reddeer.editor.finder.FigureFinder;

/**
 * Matcher which returns true if an edit part has a given label.
 * 
 * @author apodhrad
 * 
 */
public class WithLabel extends BaseMatcher<EditPart> {

	private String label;

	public WithLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof GraphicalEditPart) {
			GraphicalEditPart gep = (GraphicalEditPart) obj;
			if (gep.isSelectable()) {
				List<IFigure> labels = new FigureFinder().find(gep.getFigure(), new IsInstanceOf(
						Label.class));
				for (IFigure figure : labels) {
					String label = ((Label) figure).getText();
					if (this.label.trim().equals(label.trim())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with label '" + label + "'");
	}

}
