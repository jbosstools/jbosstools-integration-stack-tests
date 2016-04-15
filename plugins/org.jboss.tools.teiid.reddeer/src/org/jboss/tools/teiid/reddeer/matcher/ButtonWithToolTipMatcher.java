package org.jboss.tools.teiid.reddeer.matcher;


import org.eclipse.swt.widgets.Button;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ButtonWithToolTipMatcher extends BaseMatcher<Button> {
	private String toolTip;
	
	public ButtonWithToolTipMatcher(String toolTip){
		this.toolTip = toolTip;
	}
	
	@Override
	public boolean matches(Object o) {
		if (o instanceof Button){
			String toolTip = ((Button) o).getToolTipText();
			if (toolTip != null){
				return toolTip.equals(this.toolTip);
			}
		}
		return false;
	}
	
	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("button with tooltip "+ toolTip);
	}
}
