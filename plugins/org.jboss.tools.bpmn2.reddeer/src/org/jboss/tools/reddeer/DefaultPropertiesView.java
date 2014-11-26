package org.jboss.tools.reddeer;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.bpmn2.reddeer.AbsoluteEditPart;
import org.jboss.tools.reddeer.matcher.WidgetWithClassName;

/**
 * 
 */
public class DefaultPropertiesView extends WorkbenchView {

	private SWTBot bot = new SWTBot();
	private AbsoluteEditPart element;
	
	/**
	 * 
	 */
	public DefaultPropertiesView(AbsoluteEditPart element) {
		super("General", "Properties");
		this.element = element;
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
	
	private List<String> getPropertiesTabs(){
		final List<String> result = new ArrayList<String>();
		
		try {
			final Composite widget = (Composite) bot.widget(new WidgetWithClassName("TabbedPropertyList"));
						
			if(widget != null) {
				
				Display.syncExec(new Runnable() {

					@Override
					public void run() {
						for(Control listItem : (Control[]) widget.getChildren()) {
							result.add(listItem.toString());
						}
					}
				});
				
			}
		} catch (WidgetNotFoundException e) {
			// empty list will be returned
		}
		
		return result;
	}
	
	private class WaitForPropertiesTab implements WaitCondition {
		
		private List<String> oldLabels;
		
		public WaitForPropertiesTab(List<String> oldLabels) {
			this.oldLabels = oldLabels;
		}
		
		@Override
		public boolean test() {
			List<String> newLabels = getPropertiesTabs();
			newLabels.removeAll(oldLabels);
			return !newLabels.isEmpty();
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
		
		List<String> oldLabels = getPropertiesTabs();
		
		element.click();
		activate();
		
		new WaitUntil(new WaitForPropertiesTab(oldLabels), TimePeriod.getCustom(5), false);
			
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
