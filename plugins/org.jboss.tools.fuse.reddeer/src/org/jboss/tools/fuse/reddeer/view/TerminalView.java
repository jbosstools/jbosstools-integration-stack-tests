package org.jboss.tools.fuse.reddeer.view;

import java.io.IOException;

import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.fuse.reddeer.utils.ShellManager;

import com.jcraft.jsch.JSchException;

/**
 * Performs operations with the <i>Fuse Shell</i> in <i>Terminal</i> view.
 * Command execution is performed via SSH (not via JBoss Fuse Tooling).
 * 
 * @author tsedmik
 */
public class TerminalView extends WorkbenchView {

	public TerminalView() {
		super("Terminal", "Terminal");
	}

	/**
	 * Types a given command into the Fuse Shell
	 * 
	 * @param command command that will be performed
	 */
	public String execute(String command) {
		
		ShellManager shell = null;
		try {
			shell = new ShellManager("admin", "admin", "0.0.0.0", 8101);
			return shell.execute(command);
		} catch (JSchException e) {
			log.error("Cannot create ShellManager");
		} catch (IOException e) {
			log.error("Reading response to given command error");
		} finally {
			if (shell != null) shell.close();
		}
		
		return null;
	}

	/**
	 * Tries to connect to the Fuse shell
	 */
	public void connect() {
		
		AbstractWait.sleep(TimePeriod.getCustom(2));
		new ViewToolItem("Connect").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}
	
	/**
	 * Creates a new Local Fabric
	 */
	public void createFabric() {
		
		execute("fabric:create");
	}
	
	/**
	 * Checks whether JBoss Fuse log contains given text
	 * 
	 * @param text Text which presence is checked in JBoss Fuse log
	 * @return true - text is in the log, false - otherwise
	 */
	public boolean containsLog(String text) {
		
		ShellManager shell = null;
		int attempts = 10;
		
		try {
			shell = new ShellManager("admin", "admin", "0.0.0.0", 8101);
			while (attempts-- > 0) {
				String tmp = shell.execute("log:display");
				System.out.println(tmp);
				if (tmp.contains(text)) return true;
				AbstractWait.sleep(TimePeriod.SHORT);
			}
			return false;
		} catch (JSchException e) {
			log.error("Cannot create ShellManager");
		} catch (IOException e) {
			log.error("Problem with creating output");
		} finally {
			if (shell != null) shell.close();
		}

		return false;
	}
}
