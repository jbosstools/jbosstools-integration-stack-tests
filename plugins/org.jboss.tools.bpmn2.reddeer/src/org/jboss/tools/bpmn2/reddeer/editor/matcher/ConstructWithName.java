package org.jboss.tools.bpmn2.reddeer.editor.matcher;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 *
 * @param <T>
 */
public class ConstructWithName<T extends EditPart> extends BaseMatcher<EditPart> {

	private static String METHOD_NAME = "getName";

	private Pattern pattern;

	/**
	 * 
	 * @param name
	 */
	public ConstructWithName(String name) {
		this.pattern = Pattern.compile(name);
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			return matches((EditPart) item, pattern);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" construct label matches '" + pattern + "'");
	}

	/**
	 * 
	 * @param editPart
	 * @param name
	 * @return
	 */
	public static boolean matches(EditPart editPart, Pattern pattern) {
		Object model = editPart.getModel();
		// Check weather the process edit part attribute matches the pattern.
		//
		// See org.eclipse.graphiti.mm.pictograms.impl.DiagramImpl;
		if (isNamePatternMatch(model, pattern)) {
			return true;
		}
		// Check weather a business object attribute matches the patterns.
		//
		// See org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl.getLink();
		if (isShape(model) && !isLabel(editPart)) {
			Shape shape = (Shape) model;
			PictogramLink link = shape.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					// Make sure we do not include pictogram labels. E.g. gateway has it's name under the pictogram
					// and this is a separate object which does not allow actions to be taken on it.
					//
					// A label object is represented by the GFMultilineText and must be the only child! Other objects
					// may have a label too but these are complex objects not just simple labels.
					if (isNamePatternMatch(eo, pattern) && !isLabel(editPart)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isShape(Object object) {
		return (object instanceof Shape);
	}

	/**
	 * 
	 * @param editPart
	 * @return
	 */
	public static boolean isLabel(EditPart editPart) {
		String className = "org.eclipse.graphiti.ui.internal.figures.GFMultilineText";
		IFigure figure = ((GraphicalEditPart) editPart).getFigure();
		@SuppressWarnings("unchecked")
		List<Object> children = figure.getChildren();

		return (children.size() == 1 && children.get(0).getClass().getName().equals(className));
	}

	/**
	 * 
	 * @param instance
	 * @param pattern
	 * @return
	 */
	public static boolean isNamePatternMatch(Object object, Pattern pattern) {
		try {
			Method method = object.getClass().getMethod(METHOD_NAME);
			String name = method.invoke(object).toString();

			return pattern.matcher(name).matches();
		} catch (Exception e) {
			// If thrown then the object does not match.
			return false;
		}
	}
}
