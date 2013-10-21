package org.jboss.tools.fuse.reddeer.editor;

import java.util.List;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.tools.fuse.reddeer.editor.finder.FigureFinder;
import org.jboss.tools.fuse.reddeer.editor.matcher.ContextButtonMatcher;

/**
 * Represents a context button in graphiti framework.
 * 
 * @author apodhrad
 * 
 */
public class ContextButton {

	private Clickable contextButton;

	public ContextButton(final String label) {
		IFigure parent = new GefEditor().getFigureCanvas().getContents();
		List<IFigure> figures = new FigureFinder().find(parent, new ContextButtonMatcher(label));
		if (figures.isEmpty()) {
			throw new RuntimeException("Couldn't found context button with label '" + label + "'");
		}
		contextButton = (Clickable) figures.get(0);
	}

	public void click() {
		Display.asyncExec(new Runnable() {

			@Override
			public void run() {
				contextButton.doClick();
			}

		});
	}
}
