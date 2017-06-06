package org.jboss.tools.common.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.swt.impl.menu.ViewMenu;

/**
 * Represents <i>Error Log</i> view
 * 
 * @author tsedmik
 */
public class ErrorLogView extends LogView {

	public void selectActivateOnNewEvents(boolean value) {
		open();
		ViewMenu menu = new ViewMenu("View Menu", "Activate on new events");
		if ((value && !menu.isSelected()) || (!value && menu.isSelected())) {
			menu.select();
		}
	}
}
