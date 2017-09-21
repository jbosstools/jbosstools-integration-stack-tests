package org.jboss.tools.common.reddeer.debug;

import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

/**
 * Represents 'Suspend' button
 * 
 * @author tsedmik
 */
public class SuspendButton extends ShellMenuItem {

	public SuspendButton() {

		super(new WorkbenchShell(), "Run", "Suspend");
	}

}
