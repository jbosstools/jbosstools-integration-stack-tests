package org.jboss.tools.reddeer;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

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
		Widget w = bot.widget(new BaseMatcher<Widget>() {
			
			public boolean matches(Object item) {
				return item.getClass().getSimpleName().equals("TabbedPropertyList");
			}
			
			public void describeTo(Description description) {
				// no operation
			}
			
		});
		
		try {
			Method m = w.getClass().getMethod("select", int.class, boolean.class);
			m.invoke(w, value, true);
		} catch (Exception e) {
			throw new IllegalStateException("TabbedPropertyList object was not found!");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSelectionIndex() {
		Widget w = bot.widget(new BaseMatcher<Widget>() {
			
			public boolean matches(Object item) {
				return item.getClass().getSimpleName().equals("TabbedPropertyList");
			}
			
			public void describeTo(Description description) {
				// no operation
			}
			
		});
		
		try {
			Method m = w.getClass().getMethod("getSelectionIndex");
			return (Integer) m.invoke(w);
		} catch (Exception e) {
			throw new IllegalStateException("TabbedPropertyList object was not found!");
		}
	}
	
	/**
	 * 
	 * @param label
	 */
	public void selectTab(String label) {
		open();
		// the resolution is to low and some tabs are not visible. if they
		// are not visible they will not be found! maximize the view to make
		// them visible
		maximize();
		new ListElement(label).select();
		// maximize second time will restore the normal size.
		maximize();
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return new DefaultCLabel().getText();
	}
	
	/**
	 * 
	 * @param bot
	 */
	protected void activateShell(SWTWorkbenchBot bot) {
		try {
			bot.activeShell();
		} catch (WidgetNotFoundException e) {
			SWTBotShell[] shellArray = bot.shells();
			for (SWTBotShell shell : shellArray) {
				if (shell.getText() != null && shell.getText().endsWith("Eclipse Platform")) {
					shell.activate();
					return;
				}
			}
			throw new WidgetNotFoundException("Main shell was not found!");
		}
	}
	
	@Override
	public void minimize() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		activateShell(bot);
		bot.menu("Window").menu("Navigation").menu("Minimize Active View or Editor").click();
	}
	
	@Override
	public void maximize() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		activateShell(bot);
		bot.menu("Window").menu("Navigation").menu("Maximize Active View or Editor").click();
	}
	
	private class ListElement {

		private Canvas canvas;

		public ListElement(String label) {
			canvas = (Canvas) bot.widget(new ListElementWithLabel(label));
		}

		public void select() {
			new ClickControl(canvas).click();
		}

	}
	
	private class ListElementWithLabel extends BaseMatcher<Widget> {

		private String label;

		public ListElementWithLabel(String label) {
			this.label = label;
		}

		public boolean matches(Object item) {
			if (item.getClass().getSimpleName().equals("ListElement")) {
				return item.toString().equals(label);
			}
			return false;
		}

		public void describeTo(Description description) {
			description.appendText("of class '").appendText(label).appendText("'");
		}
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
