package org.jboss.tools.common.reddeer.debug;

import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

/**
 * Represents 'Step Over' button
 * 
 * @author tsedmik
 */
public class StepOverButton extends ShellMenuItem {

	public StepOverButton() {

		super(new WorkbenchShell(), "Run", "Step Over");
	}
}
