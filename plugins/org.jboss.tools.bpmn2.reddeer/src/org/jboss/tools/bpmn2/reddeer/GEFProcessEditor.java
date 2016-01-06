package org.jboss.tools.bpmn2.reddeer;

import static org.hamcrest.Matchers.allOf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.SWT;
import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.GEFLayerException;
import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOfType;
import org.jboss.tools.bpmn2.reddeer.finder.AllChildEditPartFinder;
import org.jboss.tools.bpmn2.reddeer.finder.AllEditPartFinder;
import org.jboss.tools.bpmn2.reddeer.matcher.EditPartOfClassName;

public class GEFProcessEditor extends GEFEditor {

	private AllEditPartFinder finder = new AllEditPartFinder();
	private AllChildEditPartFinder childFinder = new AllChildEditPartFinder();

	public List<EditPart> getAllContainerShapeEditParts(ElementType type) {
		EditPart parent = viewer.getContents();
		return finder.find(parent, allOf(createContainerMatcherList(new ConstructOfType<EditPart>(type))));
	}

	public List<EditPart> getAllContainerShapeEditParts(EditPart parent, Matcher<org.eclipse.gef.EditPart> matcher) {
		List<EditPart> result = finder.find(parent, allOf(createContainerMatcherList(matcher)));
		return result;
	}

	public List<EditPart> getAllChildContainerShapeEditParts(EditPart parent,
			Matcher<org.eclipse.gef.EditPart> matcher) {
		List<EditPart> result = childFinder.find(parent, allOf(createContainerMatcherList(matcher)));
		return result;
	}

	public List<EditPart> getAllShapeEditParts() {
		EditPart parent = viewer.getContents();
		List<EditPart> result = finder.find(parent, new EditPartOfClassName("ShapeEditPart"));
		return result;
	}

	/**
	 * Returns edit part which wraps all elements of process
	 * 
	 * @return EditPart - which is instance of some internal DiagramEditPart
	 */
	public EditPart getRootEditPart() {
		return viewer.getContents();
	}

	private List<Matcher<? super EditPart>> createContainerMatcherList(Matcher<? super EditPart> matcher) {
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
		matcherList.add(matcher);
		return matcherList;
	}

	public GraphicalViewer getViewer() {
		return viewer;
	}

	public org.jboss.reddeer.gef.api.EditPart addElementFromPalette(ElementType type, final int x, final int y,
			final EditPart parent) {
		int oldCount = getNumberOfEditParts();

		final ViewerListener viewerListener = new ViewerListener();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				parent.addEditPartListener(viewerListener);
			}
		});

		getPalette().activateTool(type.toToolPath()[1], type.toToolPath()[0]);
		click(x, y);

		new WaitUntil(new EditorHasEditParts(this, oldCount));

		if (viewerListener.getAddedEditPart() == null) {
			throw new GEFLayerException("No new edit part was detected");
		}

		return new AbsoluteEditPart(viewerListener.getAddedEditPart());
	}

	public org.jboss.reddeer.gef.api.EditPart addConnectionFromPalette(ConnectionType connectionType, Element from,
			Element to) {
		int oldCount = getNumberOfEditParts();

		final EditPart parent = getRootEditPart();

		final ViewerListener viewerListener = new ViewerListener();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				parent.addEditPartListener(viewerListener);
			}
		});

		getPalette().activateTool(connectionType.toName());
		click(from);
		click(to);
		getPalette().activateTool("Select");

		new WaitUntil(new EditorHasEditParts(this, oldCount));

		if (viewerListener.getAddedEditPart() == null) {
			throw new GEFLayerException("No new edit part was detected");
		}

		return new AbsoluteEditPart(viewerListener.getAddedEditPart());
	}

	public void click(final Element element) {
		final WidgetHandler handler = WidgetHandler.getInstance();
		final FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();

		final int centerX = element.getBounds().getCenter().x();
		final int centerY = element.getBounds().getCenter().y();

		Display.syncExec(new Runnable() {
			@Override
			public void run() {

				int canvasWidth = figureCanvas.getViewport().getBounds().width;
				int canvasHeight = figureCanvas.getViewport().getBounds().height;
				int moveX = 0;
				int moveY = 0;

				if (centerX > canvasWidth) {
					moveX = 10;
				}
				if (centerX < 0) {
					moveX = -10;
				}
				if (centerY > canvasHeight) {
					moveY = 10;
				}
				if (centerY < 0) {
					moveY = -10;
				}

				if (moveX != 0) {
					int base = 0;
					while (element.getBounds().getCenter().x() < 0
							|| element.getBounds().getCenter().x() > canvasWidth) {
						figureCanvas.scrollToX(base);
						base += moveX;
					}
				}

				if (moveY != 0) {
					int base = 0;
					while (element.getBounds().getCenter().y() < 0
							|| element.getBounds().getCenter().y() > canvasHeight) {
						figureCanvas.scrollToY(base);
						base += moveY;
					}
				}
			}
		});
		int newX = element.getBounds().getCenter().x();
		int newY = element.getBounds().getCenter().y();

		handler.notifyItemMouse(SWT.MouseMove, 0, figureCanvas, null, newX, newY, 0);
		handler.notifyItemMouse(SWT.MouseDown, 0, figureCanvas, null, newX, newY, 1);
		handler.notifyItemMouse(SWT.MouseUp, 0, figureCanvas, null, newX, newY, 1);
	}

	private class ViewerListener implements EditPartListener {

		private EditPart addedEditPart;

		public EditPart getAddedEditPart() {
			return addedEditPart;
		}

		@Override
		public void childAdded(EditPart child, int index) {

			addedEditPart = child;
		}

		@Override
		public void partActivated(EditPart editpart) {

		}

		@Override
		public void partDeactivated(EditPart editpart) {

		}

		@Override
		public void removingChild(EditPart child, int index) {

		}

		@Override
		public void selectedStateChanged(EditPart editpart) {

		}

	}

}
