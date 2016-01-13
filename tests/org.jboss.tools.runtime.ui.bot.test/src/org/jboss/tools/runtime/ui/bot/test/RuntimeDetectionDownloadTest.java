package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.tools.runtime.reddeer.preference.JBossRuntimeDetection;
import org.jboss.tools.runtime.reddeer.preference.FuseServerRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of download runtime option in <i>JBoss Runtime Detection</i> in <i>Preferences</i>
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class RuntimeDetectionDownloadTest {

	@Before
	public void clearErrorLog() {

		// delete Error Log
		LogView log = new LogView();
		log.open();
		log.clearLog();
	}

	@After
	public void clean() {

		// close all shells
		ShellHandler.getInstance().closeAllNonWorbenchShells();

		// remove all configured runtimes (in Runtime Detection)
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		prefPage.removeAllRuntimes();
		prefPage.ok();

		// remove all configured runtimes (in server runtimes)
		FuseServerRuntimePreferencePage runtimePref = new FuseServerRuntimePreferencePage();
		runtimePref.open();
		runtimePref.removeAllServerRuntimes();
		runtimePref.ok();
	}

	@Test
	public void testDownloadGateInPortal360() {
		downloadRuntime("GateIn Portal 3.6.0");
	}

	@Test
	public void testDownloadJBoss328SP1() {
		downloadRuntime("JBoss 3.2.8 SP 1");
	}

	@Test
	public void testDownloadJBoss405() {
		downloadRuntime("JBoss 4.0.5");
	}

	@Test
	public void testDownloadJBoss423() {
		downloadRuntime("JBoss 4.2.3");
	}

	@Test
	public void testDownloadJBoss501() {
		downloadRuntime("JBoss 5.0.1");
	}

	@Test
	public void testDownloadJBoss510() {
		downloadRuntime("JBoss 5.1.0");
	}

	@Test
	public void testDownloadJBoss600() {
		downloadRuntime("JBoss 6.0.0");
	}

	@Test
	public void testDownloadJBoss610() {
		downloadRuntime("JBoss 6.1.0");
	}

	@Test
	public void testDownloadJBoss701() {
		downloadRuntime("JBoss 7.0.1");
	}

	@Test
	public void testDownloadJBoss702() {
		downloadRuntime("JBoss 7.0.2");
	}

	@Test
	public void testDownloadJBoss710() {
		downloadRuntime("JBoss 7.1.0");
	}

	@Test
	public void testDownloadJBossAS711() {
		downloadRuntime("JBoss AS 7.1.1 (Brontes)");
	}

	@Test
	public void testDownloadJBossEAP600() {
		downloadRuntime("JBoss EAP 6.0.0");
	}

	@Test
	public void testDownloadJBossEAP601() {
		downloadRuntime("JBoss EAP 6.0.1");
	}

	@Test
	public void testDownloadJBossEAP610() {
		downloadRuntime("JBoss EAP 6.1.0");
	}

	@Test
	public void testDownloadJBossEAP620() {
		downloadRuntime("JBoss EAP 6.2.0");
	}

	@Test
	public void testDownloadJBossEAP630() {
		downloadRuntime("JBoss EAP 6.3.0");
	}

	@Test
	public void testDownloadJBossPortalPlatform600() {
		downloadRuntime("JBoss Portal Platform 6.0.0");
	}

	@Test
	public void testDownloadJBossPortalPlatform610() {
		downloadRuntime("JBoss Portal Platform 6.1.0");
	}

	@Test
	public void testDownloadJBossSeam202SP1() {
		downloadRuntime("JBoss Seam 2.0.2.SP1");
	}

	@Test
	public void testDownloadJBossSeam212SP1() {
		downloadRuntime("JBoss Seam 2.1.2.SP1");
	}

	@Test
	public void testDownloadJBossSeam222() {
		downloadRuntime("JBoss Seam 2.2.2");
	}

	@Test
	public void testDownloadJBossSeam231() {
		downloadRuntime("JBoss Seam 2.3.1");
	}

	@Test
	public void testDownloadWildFly800Final() {
		downloadRuntime("WildFly 8.0.0 Final");
	}

	@Test
	public void testDownloadWildFly810Final() {
		downloadRuntime("WildFly 8.1.0 Final");
	}

	@Test
	public void testDownloadKaraf2211() {
		downloadRuntime("Apache Karaf 2.2.11");
	}

	@Test
	public void testDownloadKaraf2210() {
		downloadRuntime("Apache Karaf 2.3.10");
	}

	@Test
	public void testDownloadKaraf241() {
		downloadRuntime("Apache Karaf 2.4.1");
	}

	@Test
	public void testDownloadKaraf303() {
		downloadRuntime("Apache Karaf 3.0.3");
	}

	@Test
	public void testDownloadServiceMix453() {
		downloadRuntime("Apache ServiceMix 4.5.3");
	}

	@Test
	public void testDownloadServiceMix506() {
		downloadRuntime("Apache ServiceMix 5.0.6");
	}

	@Test
	public void testDownloadServiceMix514() {
		downloadRuntime("Apache ServiceMix 5.1.4");
	}

	@Test
	public void testDownloadServiceMix520() {
		downloadRuntime("Apache ServiceMix 5.2.0");
	}

	@Test
	public void testDownloadServiceMix531() {
		downloadRuntime("Apache ServiceMix 5.3.1");
	}

	@Test
	public void testDownloadServiceMix540() {
		downloadRuntime("Apache ServiceMix 5.4.0");
	}

	@Test
	public void testDownloadFuse600() {
		downloadRuntime("JBoss Fuse 6.0.0");
	}

	@Test
	public void testDownloadFuse610() {
		downloadRuntime("JBoss Fuse 6.1.0");
	}

	protected static void downloadRuntime(String name) {
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		DownloadRuntimesWizard runtimeWiz = prefPage.downloadRuntime();
		runtimeWiz.selectRuntime(name);
		runtimeWiz.next();
		runtimeWiz.acceptTerms();
		runtimeWiz.next();

		runtimeWiz.setInstallFolder(getPath() + "target/temp_install");
		runtimeWiz.setDownloadFolder(getPath() + "target/temp");
		runtimeWiz.finish(name);
		assertEquals(1, prefPage.getRuntimesCount());
		prefPage.cancel();
		FuseServerRuntimePreferencePage runtimePref = new FuseServerRuntimePreferencePage();
		runtimePref.open();
		assertEquals(1, runtimePref.getServerRuntimes().size());
		runtimePref.cancel();
		new LogView().open();
		assertTrue(new LogView().getErrorMessages().size() == 0);
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
