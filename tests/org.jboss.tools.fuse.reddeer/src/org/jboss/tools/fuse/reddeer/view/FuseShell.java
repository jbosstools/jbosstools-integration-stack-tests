package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.Keyboard;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Performs operations with the <i>Fuse Shell</i> view.
 * 
 * @author tsedmik
 */
public class FuseShell extends WorkbenchView {

	public FuseShell() {
		super("Fuse", "Shell");
	}

	/**
	 * Types a given command into the Fuse Shell
	 * 
	 * @param command command that will be performed
	 */
	public void execute(String command) {
		
		new Keyboard() {
			@Override public void writeToClipboard(boolean cut) {}
			@Override public void pasteFromClipboard() {}
		}.type(command + "\n");
	}

	/**
	 * Tries to connect to the Fuse shell
	 */
	public void connect() {
		
		AbstractWait.sleep(TimePeriod.getCustom(2));
		new DefaultToolItem("Connect").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}
	
	/**
	 * Creates a new Local Fabric
	 */
	public void createFabric() {
		
		open();
		connect();
		execute("fabric:create");
		AbstractWait.sleep(TimePeriod.getCustom(30)); // TODO replace it with wait condition
	}
	
	
}
