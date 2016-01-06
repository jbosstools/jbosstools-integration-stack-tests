package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.core.handler.ButtonHandler;
import org.jboss.reddeer.core.util.Display;

/**
 * Gets all buttons with the same tooltip (e.g. radio buttons column/constant/function in criteria builder)
 * 
 * @author lfabriko
 *
 */
public class ToolBarButtonWithLabel extends BaseMatcher<ToolItem> {

	/*
	 * private String toolTip;
	 * 
	 * public ButtonWithToolTip(String toolTip){ this.toolTip = toolTip; }
	 * 
	 * @Override public boolean matches(Object o) { if (o instanceof Button){ Button but = (Button)o; if
	 * (but.getToolTipText().equals(this.toolTip)){ return true; } } return false; }
	 * 
	 * @Override public void describeTo(Description arg0) {
	 * 
	 * }
	 */

	// private class ToolBarButtonWithLabel extends BaseMatcher {

	private Button but;
	private String label;

	public ToolBarButtonWithLabel() {
		this.label = "";
	}

	public ToolBarButtonWithLabel(String label) {
		this.label = label;

	}

	@Override
	public boolean matches(Object o) {// ToolItem
		if (o instanceof ToolItem) {
			ToolItem ti = (ToolItem) o;
			if (ti.getControl() instanceof Button) {
				but = (Button) ti.getControl();
				if (but.getText().equals(label)) {
					System.out.println(but.getText());
					Display.syncExec(new Runnable() {
						@Override
						public void run() {
							ButtonHandler.getInstance().click(but);
						}
					});
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description arg0) {

	}

}
