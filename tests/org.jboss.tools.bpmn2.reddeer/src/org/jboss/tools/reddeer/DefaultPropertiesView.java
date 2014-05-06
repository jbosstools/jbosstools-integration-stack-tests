package org.jboss.tools.reddeer;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.reddeer.matcher.WidgetWithClassName;

/**
 * 
 */
public class DefaultPropertiesView extends WorkbenchView {

	private SWTBot bot = new SWTBot();
	
	/**
	 * 
	 */
	public DefaultPropertiesView() {
		super("General", "Properties");
		open();
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setSelectionIndex(int value) {
		invokeMethod("select", value, true);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSelectionIndex() {
		return (Integer) invokeMethod("getSelectionIndex");
	}
	
	private Object invokeMethod(String name, Object ... args) {
		Widget w = bot.widget(new WidgetWithClassName("TabbedPropertyList"));
		
		if (w == null) {
			throw new IllegalStateException("Properties view tab list not found.");
		}
		
		Class<?>[] argTypes = new Class<?>[args.length];
		for (int i=0; i<args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		
		try {
			Method m = w.getClass().getMethod(name, argTypes);
			return m.invoke(w, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param label
	 */
	public void selectTab(String label) {
		open();
		try {
			// When the resolution is low some tabs are not visible. Unless they
			// are rendered at least once, they will not be found by SWTBot.
			// Maximize the view to force rendering of hidden tabs.
			maximize();
			// Attempt to get the tab with given label.
			Canvas c = getListElement(label);
			if (c == null) {
				throw new WidgetNotFoundException("Element with text '" + label + "' not found.");
			}
			// Select the tab.
			new ClickControl(c).click();
		} finally {
			// Maximizing the view a second time will restore the original size.
			maximize();
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return new DefaultCLabel().getText();
	}
	
	private Canvas getListElement(String label) {
		List<? extends Widget> listElements = bot.widgets(new WidgetWithClassName("ListElement"));
		for (Widget e : listElements) {
			if (e.toString().equals(label)) {
				return (Canvas) e;
			}
		}
		return null;
	}
	
	private class ClickControl extends AbstractSWTBotControl<Composite> {

		public ClickControl(Composite w) {
			super(w);
		}

		public ClickControl click() {
			waitForEnabled();
			notify(SWT.MouseDown);
			notify(SWT.MouseUp);
			return this;
		}
	}
	
}
