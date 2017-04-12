package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.common.reddeer.FileUtils;
import org.jboss.tools.common.reddeer.condition.RadioButtonIsAvailable;
import org.jboss.tools.common.reddeer.widget.LabeledTextExt;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.runtime.reddeer.preference.FuseServerRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.preference.JBossRuntimeDetection;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class contains test cases on a variety of server feature requests
 * 
 * @author djelinek
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class FeaturesServerTest extends DefaultTest {

	protected Logger log = Logger.getLogger(CamelEditorTest.class);

	@Before
	public void clean() {
		// clean previous runtime installations
		FileUtils.deleteDir(new File(getPath() + "target/temp_install/jboss-fuse-6.3.0.redhat-187"));
		// close all shells
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	/**
	 * <p>
	 * Fuse runtime detection never makes a server
	 * <p>
	 * <ul>
	 * <li>Download and install new runtime in JBoss Runtime Detection</li>
	 * <li>Verify if new server was created</li>
	 * <li>Open Fuse Runtime Preference Page</li>
	 * <li>Verify if new runtime was created</li>
	 * </ul>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-2176">https://issues.jboss.org/browse/FUSETOOLS-2176</a>
	 */
	@Test
	public void testFuseJbossRuntimeDetection() {
		downloadRuntime("JBoss Fuse 6.3.0");
		assertTrue("Server was not created in view", new ServersView().getServers().size() == 1);
		FuseServerRuntimePreferencePage runtimePref = new FuseServerRuntimePreferencePage();
		runtimePref.open();
		assertTrue("Server runtime was not created", runtimePref.getServerRuntimes().size() == 1);
	}

	/**
	 * Downloads/installs new runtime into temp/temp_install folder
	 * 
	 * @param name
	 *            String name of runtime
	 */
	private void downloadRuntime(String name) {
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		DownloadRuntimesWizard runtimeWiz = prefPage.downloadRuntime();
		runtimeWiz.selectRuntime(name);
		runtimeWiz.next();
		new PushButton("Add...").click();
		/**
		 * temporary solution ... credentials should not be in public repository -> use system parameters or something
		 * else solution
		 */
		new LabeledTextExt("Username:").setText("aqtwrdnr");
		new LabeledTextExt("Password:").setText("aqtwrdnr");
		new PushButton("OK").click();
		runtimeWiz.next();
		new WaitUntil(new ShellWithTextIsAvailable("Download Runtimes"));
		new WaitUntil(new RadioButtonIsAvailable("I accept the terms of the license agreement"));
		runtimeWiz.next();
		runtimeWiz.setInstallFolder(getPath() + "target/temp_install");
		runtimeWiz.setDownloadFolder(getPath() + "target/temp");
		runtimeWiz.finish(name);
		new WaitWhile(new ShellWithTextIsAvailable("Download 'JBoss Fuse 6.3.0'"), TimePeriod.getCustom(900));
		prefPage.ok();
	}

	/**
	 * Returns absolute path to the project
	 * 
	 * @return absolute path to the project
	 */
	private static String getPath() {
		File currentDirFile = new File(".");
		String temp = currentDirFile.getAbsolutePath();
		return temp.substring(0, temp.length() - 1);
	}

}
