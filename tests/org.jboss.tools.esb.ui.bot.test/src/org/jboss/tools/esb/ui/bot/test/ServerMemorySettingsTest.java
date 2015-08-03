package org.jboss.tools.esb.ui.bot.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.direct.preferences.Preferences;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 * 
 */
@RunWith(RedDeerSuite.class)
@Server(type = ServerReqType.EAP, state = ServerReqState.STOPPED)
public class ServerMemorySettingsTest {

	public Logger log = Logger.getLogger(ServerMemorySettingsTest.class);

	@InjectRequirement
	private ServerRequirement serverRequirement;

	private static String limitOfConsoleOutput;

	@BeforeClass
	public static void turnOffLimitOfConsoleOutput() {
		limitOfConsoleOutput = Preferences.get("org.eclipse.debug.ui", "Console.limitConsoleOutput");
		Preferences.set("org.eclipse.debug.ui", "Console.limitConsoleOutput", "false");
	}

	@AfterClass
	public static void setBackLimitOfConsoleOutput() {
		if (limitOfConsoleOutput != null) {
			Preferences.set("org.eclipse.debug.ui", "Console.limitConsoleOutput", limitOfConsoleOutput);
		}
	}

	@Test
	public void serverMemorySettingsTest() {
		serverRequirement.getConfig().getServerBase().setState(ServerReqState.RUNNING);

		ConsoleView consoleView = new ConsoleView();
		String consoleText = consoleView.getConsoleText();

		Assert.assertTrue(consoleText.contains("-Xms1303m -Xmx1303m -XX:MaxPermSize=256m"));
	}

}
