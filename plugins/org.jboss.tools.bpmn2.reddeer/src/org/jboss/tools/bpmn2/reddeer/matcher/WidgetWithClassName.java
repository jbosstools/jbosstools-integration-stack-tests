package org.jboss.tools.bpmn2.reddeer.matcher;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
  * 
  */
public class WidgetWithClassName extends BaseMatcher<Widget> {

	private String simpleClassName;

	/**
	 * 
	 * @param simpleClassName
	 */
	public WidgetWithClassName(String simpleClassName) {
		this.simpleClassName = simpleClassName;
	}

	/**
	 * 
	 * @param clazz
	 */
	public WidgetWithClassName(Class<?> clazz) {
		this(clazz.getSimpleName());
	}

	@Override
	public boolean matches(Object item) {
		return item.getClass().getSimpleName().equals(simpleClassName);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("of class '").appendText(simpleClassName)
				.appendText("'");
	}

}