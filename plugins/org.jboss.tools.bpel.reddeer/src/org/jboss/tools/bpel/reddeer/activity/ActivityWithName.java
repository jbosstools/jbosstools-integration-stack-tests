package org.jboss.tools.bpel.reddeer.activity;

import java.lang.reflect.Method;

import org.eclipse.gef.EditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class ActivityWithName extends BaseMatcher<EditPart> {

	private String name;

	public ActivityWithName(String name) {
		this.name = name;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			EditPart editPart = (EditPart) item;
			String activityName = getActivityName(editPart);
			return activityName != null && activityName.equals(name);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("activity with name '" + name + "'");
	}

	public static String getActivityName(EditPart editPart) {
		Object model = editPart.getModel();
		Class<?> clazz = model.getClass();

		try {
			Method method = clazz.getMethod("getName");
			Object result = method.invoke(model);
			return (String) result;
		} catch (Exception e) {
			return null;
		}
	}

}