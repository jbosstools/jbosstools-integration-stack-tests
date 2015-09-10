package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Before;

public class KieNavigatorTestParent extends TestParent {
	
	private static final Logger LOGGER = Logger.getLogger(KieNavigatorTestParent.class);
	 
	private static final int MAX_WAIT_TIME = 60;
	
	private static final int HTTP_OK = 200;
	
	private static final String APP_URL = "http://localhost:8080/business-central/";
	
	@Before
	public void waitForServer() throws IOException {
		URL url = new URL(APP_URL); 
		int time = 0;
		int responceCode = 0;
		while (time < MAX_WAIT_TIME) {
			HttpURLConnection huc = (HttpURLConnection) url.openConnection(); 
			huc.setRequestMethod("GET"); 
			huc.connect(); 
			responceCode = huc.getResponseCode();
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
	}

}
