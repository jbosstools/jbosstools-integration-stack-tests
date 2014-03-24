package org.jboss.tools.fuse.ui.bot.test.utils;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.tools.fuse.reddeer.preference.SSH2PreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.ui.bot.test.Activator;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement;

/**
 * Configure a new Fuse server defined in RedDeer Requirement configuration XML file.
 * 
 * @author tsedmik
 */
public class ServerConfig {
	
	private ServerRequirement serverRequirement;
	
	public ServerConfig(ServerRequirement serverRequirement) {
		this.serverRequirement = serverRequirement;
	}

	/**
	 * Creates and configures a new Fuse server. Contains these steps:
	 * <ol>
	 * <li>Sets different location of SSH home than default.</li>
	 * <li>Copies file <i>host.key</i> to a <i>${FUSE_SERVER_HOME}/etc/</i>.</li>
	 * <li>Adds a new Fuse server</li>
	 * </ol>
	 */
	public void configureNewServer() {
		
		setSSHHome();
		copyHostKey();
		addServer();
	}
	
	/**
	 * Configures environment for creating new Fuse servers. Contains these steps:
	 * <ol>
	 * <li>Sets different location of SSH home than default.</li>
	 * <li>Copies file <i>host.key</i> to a <i>${FUSE_SERVER_HOME}/etc/</i>.</li>
	 * </ol>
	 */
	public void configureServerEnvironment() {
		
		setSSHHome();
		copyHostKey();
	}
	
	/**
	 * Sets different location of SSH home than default <i>(~/.ssh)</i> due to a smooth server start.
	 * Smooth start of the server is also dependent on method <i>copyHostKey()</i>. If <i>setSSHHome()</i>
	 * or <i>copyHostKey()</i> is not executed before a Fuse server starts, a native system window with
	 * prompt to store SSH key of the server is appeared => tests can't handle with this window.
	 */
	private void setSSHHome() {
		
		SSH2PreferencePage sshPreferencePage = new SSH2PreferencePage();
		sshPreferencePage.open();
		sshPreferencePage.setSSH2Home(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/.ssh"));
		sshPreferencePage.ok();
	}
	
	/**
	 * Copies file <i>host.key</i> to a <i>${FUSE_SERVER_HOME}/etc/</i>.
	 * Smooth start of the server is also dependent on method <i>setSSHHome()</i>. If <i>setSSHHome()</i>
	 * or <i>copyHostKey()</i> is not executed before a Fuse server starts, a native system window with
	 * prompt to store SSH key of the server is appeared => tests can't handle with this window.
	 */
	private void copyHostKey() {
		
		File src = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/serverConfig/host.key"));
		File dst = new File(serverRequirement.getPath() + "/etc/host.key");
				
		try {
			copyFileUsingStream(src, dst);
		} catch (IOException e) {
			fail("Can't copy 'host.key' file!");
		}
	}
	
	/**
	 * Add a new Fuse server.
	 */
	private void addServer() {
		
		ServerManipulator.addServerRuntime(serverRequirement.getRuntime(), serverRequirement.getPath());
		ServerManipulator.addServer(serverRequirement.getType(), serverRequirement.getHostname(),
				serverRequirement.getName(), serverRequirement.getPort(), serverRequirement.getUsername(),
				serverRequirement.getPassword());
	}
	
	/**
	 * Copies a source file to given destination
	 * 
	 * @param source Source file
	 * @param dest Destination
	 * @throws IOException
	 */
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
}
