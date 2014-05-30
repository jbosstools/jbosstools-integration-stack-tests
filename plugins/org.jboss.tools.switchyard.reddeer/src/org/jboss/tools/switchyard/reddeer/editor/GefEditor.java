package org.jboss.tools.switchyard.reddeer.editor;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.GraphicalViewer;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class GefEditor extends DefaultEditor {

	protected GraphicalViewer viewer;

	public GefEditor() {
		super();
		init();
	}

	public GefEditor(String title) {
		super(title);
		init();
	}

	private void init() {
		viewer = Display.syncExec(new ResultRunnable<GraphicalViewer>() {
			@Override
			public GraphicalViewer run() {
				return (GraphicalViewer) getEditorPart().getAdapter(GraphicalViewer.class);
			}

		});
		if (viewer == null) {
			throw new RuntimeException("Cannot find graphical viewer which is needed for GEF");
		}
	}

	public FigureCanvas getFigureCanvas() {
		return (FigureCanvas) viewer.getControl();
	}

}
