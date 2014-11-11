package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.tools.runtime.reddeer.preference.JBossRuntimeDetection;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesWizard;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of <i>JBoss Runtime Detection</i> in <i>Preferences</i>
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class RuntimeDetectionTest {

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
