package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;
import org.jboss.tools.drools.ui.bot.test.util.RestClient;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Before;

public class KieNavigatorTestParent extends TestParent {
	
	private static final Logger LOGGER = Logger.getLogger(KieNavigatorTestParent.class);
	 
	private static final int MAX_WAIT_TIME = 60;
	
	private static final int HTTP_OK = 200;
	
	protected static final String REPO_URL = "https://github.com/jboss-developer/jboss-brms-repository.git";
	
	protected static final String WEB_APP_NAME = "business-central";
	
	protected static final String USERNAME = "testadmin";
	
	protected static final String PASSWORD = "admin1234;";
	
	protected static final String HTTP_PORT = "8080";
	
	protected static final String GIT_PORT = "8001";
	
	protected static final String GIT_DIR_NAME = "tmpgit";
	
	private String gitDirectory = "";
	
	@Before
	public void waitForServer() throws IOException {
		int time = 0;
		int responceCode = 0;
		while (time < MAX_WAIT_TIME) {
			responceCode = RestClient.getResponseCode();
			LOGGER.debug("Waiting for server. Responce code: " + responceCode);
			if (responceCode == HTTP_OK) {
				break;
			}
			waitASecond();
			time += 1;
		}
		if (responceCode != HTTP_OK) {
			throw new RuntimeException("Server application is not available.");
		}
		
		// disable console to be view on top
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.toggleShowConsoleOnStandardOutChange(false);
		
		gitDirectory = createTempDir(GIT_DIR_NAME);
	}
	
	protected void setCorrectServerProperties(ServerProperties sp) {
		sp.setUsername(USERNAME);
		sp.setPassword(PASSWORD);
		sp.setApplicationName(WEB_APP_NAME);
		sp.setHttpPort(HTTP_PORT);
		sp.setGitPort(GIT_PORT);
		sp.setGitRepoPath(gitDirectory);
	}
}
