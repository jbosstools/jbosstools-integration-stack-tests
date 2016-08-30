package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.core.handler.ButtonHandler;
import org.jboss.reddeer.core.util.Display;

public class ToolBarButtonWithLabel extends BaseMatcher<ToolItem> {
	private String label;
	private Boolean toggle;

	public ToolBarButtonWithLabel(String label) {
		this.label = label;
		toggle = null;
	}
	
	public ToolBarButtonWithLabel(String label, boolean toggle) {
		this.label = label;
		this.toggle = toggle;
	}

	@Override
	public boolean matches(Object o) {
		if (o instanceof ToolItem) {
			ToolItem ti = (ToolItem) o;
			if (ti.getControl() instanceof Button) {
				final Button but = (Button) ti.getControl();
				if (but.getText().equals(label)) {
					Display.syncExec(new Runnable() {
						@Override
						public void run() {
							ButtonHandler.getInstance().click(but);
							if (toggle != null){
								but.setSelection(toggle);	
							}
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
