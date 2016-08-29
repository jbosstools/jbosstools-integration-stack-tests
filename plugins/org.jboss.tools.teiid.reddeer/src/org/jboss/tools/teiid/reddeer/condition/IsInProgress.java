package org.jboss.tools.teiid.reddeer.condition;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.lookup.ShellLookup;

/**
 * Condition that specifies if a progress window is still present
 * 
 * @author apodhrad
 * 
 */
public class IsInProgress extends AbstractWaitCondition {

	public static final String DIALOG_TITLE = "Progress Information";
	public static final String ERROR_TITLE = "Error creating tables in view model AccountView";

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean test() {
		log.debug("Looking for shell with title matching '" + DIALOG_TITLE + "'");
		Shell shell = ShellLookup.getInstance().getShell(DIALOG_TITLE, TimePeriod.NONE);
		return shell != null;
	}

	@Override
	public String description() {
		return "Process still in progress...";
	}
}

/*
package org.jboss.tools.teiid.reddeer.condition;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class IsInProgress extends AbstractWaitCondition {

	public static final String DIALOG_TITLE = "Progress Information";
	public static final String ERROR_TITLE = "Error creating tables in view model AccountView";

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean test() {
		checkError();
		try {
			new DefaultShell(DIALOG_TITLE);
			return true;
		} catch (Exception e) {
			// ok, not in progress
			return false;
		}
	}

	@Override
	public String description() {
		return "Process still in progress...";
	}

	protected void checkError() {
		try {
			new DefaultShell(ERROR_TITLE);
			log.warn(ERROR_TITLE);
			new PushButton("OK").click();
		} catch (Exception e) {
			// ok, there is no error dialog
		}
	}

}
*/