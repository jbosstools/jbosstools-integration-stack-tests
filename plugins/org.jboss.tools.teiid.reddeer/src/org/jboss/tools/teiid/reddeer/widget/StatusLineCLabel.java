package org.jboss.tools.teiid.reddeer.widget;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.reference.DefaultReferencedComposite;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class StatusLineCLabel extends DefaultCLabel {

	public StatusLineCLabel(){
		// couldn't find any easier way to get to the status line text
		super(new DefaultReferencedComposite(WidgetLookup.getInstance().activeWidget(new DefaultShell(), Composite.class, 0, new StatusLineMatcher())));
		
	}
	
	private static class StatusLineMatcher extends BaseMatcher<Widget>{

		@Override
		public boolean matches(Object widget) {
			// package private class, need to match by name
			return "org.eclipse.jface.action.StatusLine".equals(widget.getClass().getName());
		}

		@Override
		public void describeTo(Description desc) {
			desc.appendText("StatusLine");
		}
	}
}
