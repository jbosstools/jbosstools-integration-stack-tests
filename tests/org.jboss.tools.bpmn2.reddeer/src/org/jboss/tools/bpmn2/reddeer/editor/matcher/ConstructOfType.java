package org.jboss.tools.bpmn2.reddeer.editor.matcher;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 *
 */
public class ConstructOfType<T extends EditPart> extends BaseMatcher<EditPart> {

	private String constructTypeName;

	/**
	 * 
	 * @param constructType
	 */
	public ConstructOfType(ConstructType constructType) {
		this.constructTypeName = constructType.toId();
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			return equalsType((EditPart) item, constructTypeName);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" construct of type '" + constructTypeName + "'");
	}
	
	/**
	 * 
	 * @param editPart
	 * @param editPartType
	 * @return
	 */
	public static boolean equalsType(EditPart editPart, String constructTypeName) {
		Object model = editPart.getModel();
		if (ConstructType.PROCESS.toId().equals(constructTypeName)) {
			if (model instanceof Diagram) {
				return true;
			}
		} else {
			if (model instanceof Shape) {
				PictogramLink link = ((Shape) model).getLink();
				if (link != null && !isLabel(editPart)) {
					EList<EObject> objectList = link.getBusinessObjects();
					for (EObject eo : objectList) {
						return isType(eo, constructTypeName);
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * A label is of type org.eclipse.graphiti.ui.internal.figures.GFMultilineText,
	 * so return false if it is.
	 * 
	 * @param className
	 * @return
	 */
	public static boolean isLabel(EditPart editPart) {
			String className = "GFMultilineText";
			IFigure figure = ((GraphicalEditPart) editPart).getFigure();
			@SuppressWarnings("unchecked")
			List<Object> children = figure.getChildren();
			return (children.size() == 1 && children.get(0).getClass().getSimpleName().equals(className));
	}
	
	public static boolean isType(Object object, String simpleClassName) {
		Class<?>[] classArray = object.getClass().getInterfaces();
		if (classArray.length > 0) {
			for (Class<?> c : classArray) {
				if (c.getSimpleName().equals(simpleClassName)) {
					return true;
				}
			}
		}
		return false;
	}
	
}