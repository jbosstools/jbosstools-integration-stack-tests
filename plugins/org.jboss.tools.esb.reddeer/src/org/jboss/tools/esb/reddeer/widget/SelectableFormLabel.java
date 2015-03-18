package org.jboss.tools.esb.reddeer.widget;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

/**
 * 
 * @author apodhrad
 *
 */
public class SelectableFormLabel extends DefaultLabel {

	public SelectableFormLabel(String text) {
		super(text);
	}

	public void select() {
		WidgetHandler.getInstance().notify(SWT.Selection, swtWidget);
	}
}
