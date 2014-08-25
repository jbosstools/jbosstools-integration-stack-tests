package org.jboss.tools.reddeer;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.WaitUntil;
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
	
	private Control getChildren(final String label) {
		return Display.syncExec(new ResultRunnable<Control>() {

			@Override
			public Control run() {
				final Composite widget = (Composite) bot.widget(new WidgetWithClassName("TabbedPropertyList"));
				
				for(Control listItem : (Control[]) widget.getChildren()) {
					if(listItem.toString().equals(label)) {
						return listItem;
					}
				}
				
				return null;
			}
		});
	}
	
	private class WaitForPropertiesTab implements WaitCondition {

		@Override
		public boolean test() {
			Composite widget = (Composite) bot.widget(new WidgetWithClassName("TabbedPropertyList"));

			return widget != null;
		}

		@Override
		public String description() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * 
	 * @param label
	 */
	public void selectTab(String label) {
		open();
		
		
		new WaitUntil(new WaitForPropertiesTab());
			
		//Attempt to get the tab with given label.
		Canvas canvas = (Canvas) getChildren(label);
		
		if (canvas == null) {
			throw new WidgetNotFoundException("Element with text '" + label + "' not found.");
		}
		
		// Select the tab.
		new ClickControl(canvas).click();	
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
