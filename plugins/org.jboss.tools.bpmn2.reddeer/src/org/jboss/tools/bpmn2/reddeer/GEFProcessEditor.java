package org.jboss.tools.bpmn2.reddeer;

import static org.hamcrest.Matchers.allOf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.SWT;
import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.GEFLayerException;
import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.gef.handler.EditPartHandler;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.WaitUntil;
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
		List<EditPart> result =  finder.find(parent, allOf(createContainerMatcherList(matcher)));
		return result;
	}
	
	public List<EditPart> getAllChildContainerShapeEditParts(EditPart parent, Matcher<org.eclipse.gef.EditPart> matcher) {
		List<EditPart> result =  childFinder.find(parent, allOf(createContainerMatcherList(matcher)));
		return result;
	}
	
	public List<EditPart> getAllShapeEditParts() {
		EditPart parent = viewer.getContents();
		List<EditPart> result =  finder.find(parent, new EditPartOfClassName("ShapeEditPart"));
		return result;
	}
	
	/**
	 * Returns edit part which wraps all elements of process
	 * @return EditPart - which is instance of some internal DiagramEditPart
	 */
	public EditPart getRootEditPart() {
		return viewer.getContents();
	}
	
	private List<Matcher<? super EditPart> > createContainerMatcherList(Matcher<? super EditPart> matcher) {
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
		matcherList.add(matcher);
		return matcherList;
	}
	
	public GraphicalViewer getViewer() {
		return viewer;
	}

	
	public org.jboss.reddeer.gef.api.EditPart addElementFromPalette(ElementType type, final int x, final int y, final EditPart parent) {
		int oldCount = getNumberOfEditParts();

		final ViewerListener viewerListener = new ViewerListener();
		Display.asyncExec(new Runnable() {
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
	
	public void click(final Element element) {
		WidgetHandler handler = WidgetHandler.getInstance();
		final FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
		
		final int oldX = element.getBounds().getCenter().x();
		final int oldY = element.getBounds().getCenter().y();
		
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				int horizontalSelection = figureCanvas.getHorizontalBar().getSelection();
				int verticalSelection = figureCanvas.getVerticalBar().getSelection();
				int xDiff = 0; int yDiff = 0;
				if(oldX > horizontalSelection+figureCanvas.getBounds().width) {
					xDiff = oldX - horizontalSelection+figureCanvas.getBounds().width;
				}
				if(oldX < horizontalSelection) {
					xDiff = oldX - horizontalSelection;
				}
				if(oldY > verticalSelection+figureCanvas.getBounds().height) {
					yDiff = oldY - verticalSelection+figureCanvas.getBounds().height;
				}
				if(oldY < verticalSelection) {
					yDiff = oldY - verticalSelection;
				}
				figureCanvas.scrollToX(xDiff);
				figureCanvas.scrollToY(yDiff);
			}
		});
		
		IFigure figure = EditPartHandler.getInstance().getFigure(element.getAbsoluteEditPart().getEditPart());
		final Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		int newX = bounds.getCenter().x();
		int newY = bounds.getCenter().y();
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
