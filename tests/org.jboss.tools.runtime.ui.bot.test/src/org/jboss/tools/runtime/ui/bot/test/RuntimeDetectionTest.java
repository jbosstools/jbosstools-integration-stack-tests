package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.tools.runtime.reddeer.RuntimeEntry;
import org.jboss.tools.runtime.reddeer.preference.JBossRuntimeDetection;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of <i>JBoss Runtime Detection</i> in <i>Preferences</i>
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class RuntimeDetectionTest {

	private static String pathToServers;

	@BeforeClass
	public static void setPath() {
		pathToServers = System.getProperty("reddeer.config");
	}

	@After
	public void clean() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	@Test
	public void testDownloadRuntimesProjectURLs() {
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		DownloadRuntimesWizard runtimeWiz = prefPage.downloadRuntime();
		StringBuilder badURLs = new StringBuilder();
		for (String runtime : runtimeWiz.getAllRuntimes()) {
			String url = runtimeWiz.getProjectURL(runtime);
			if (!checkURL(url))
				badURLs.append(runtime + " - " + url + "\n");
		}
		runtimeWiz.cancel();
		prefPage.cancel();
		assertEquals(badURLs.toString(), 0, badURLs.length());
	}

	@Test
	public void testDownloadRuntimesDownloadURLs() {
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		DownloadRuntimesWizard runtimeWiz = prefPage.downloadRuntime();
		StringBuilder badURLs = new StringBuilder();
		for (String runtime : runtimeWiz.getAllRuntimes()) {
			String url = runtimeWiz.getDownloadURL(runtime);
			if (!checkURL(url))
				badURLs.append(runtime + " - " + url + "\n");
		}
		runtimeWiz.cancel();
		prefPage.cancel();
		assertEquals(badURLs.toString(), 0, badURLs.length());
	}

	/**
	 * This test has following prerequisites:
	 * 
	 * 1)
	 * -Dreddeer.config must be set on root folder of
	 * https://github.com/apodhrad/server-installer.git
	 * 
	 * 2)
	 * following server runtimes have to be installed:
	 * - as-7.1.1.Final
	 * - eap-6.3.0.GA
	 * - fsw-6.0.0.GA
	 * - fuse-6.1.0.GA
	 * - soa-5.3.1.GA
	 * Hint: mvn clean install -pl as-7.1.1.Final,fuse-6.1.0.GA,soa-5.3.1.GA,eap-6.3.0.GA,fsw-6.0.0.GA
	 */
	@Test
	public void testRuntimesDetection() {

		// workaround due to native window dialog for specifying a path
		RuntimeDetectionDownloadTest.downloadRuntime("WildFly 8.1.0 Final");
		JBossRuntimeDetection prefPage = new JBossRuntimeDetection();
		prefPage.open();
		prefPage.editFirstPath(pathToServers);
		List<RuntimeEntry> entries = prefPage.searchRuntimes();

		// entries should contains following elements
		List<RuntimeEntry> temp = new ArrayList<RuntimeEntry>();
		temp.add(new RuntimeEntry("", "AS", "7.1", pathToServers + "/as-7.1.1.Final/target/jboss-as-7.1.1.Final"));
		temp.add(new RuntimeEntry("", "SOA", "6.0", pathToServers + "/fsw-6.0.0.GA/target/jboss-eap-6.1"));
		temp.add(new RuntimeEntry("", "EAP", "6.3", pathToServers + "/eap-6.3.0.GA/target/jboss-eap-6.3"));
		temp.add(new RuntimeEntry("", "FUSE6x", "6.1", pathToServers + "/fuse-6.1.0.GA/target/jboss-fuse-6.1.0.redhat-379"));
		temp.add(new RuntimeEntry("", "SOA-P", "5.3", pathToServers + "/soa-5.3.1.GA/target/jboss-soa-p-5"));
		for (RuntimeEntry item : entries) {
			if (!temp.contains(item)) {
				fail();
			}
		}

		prefPage.ok();
	}

	/**
	 * Checks whether a given URL is accessible
	 * 
	 * @param url URL address
	 * @return true - URL is accessible, false - otherwise
	 */
	private static boolean checkURL(String url) {
		if (url.equals("None"))
			return true;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestMethod("GET");
			return ((con.getResponseCode() == HttpURLConnection.HTTP_OK) || (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP));
		} catch (Exception e) {
			return false;
		}
	}
}
