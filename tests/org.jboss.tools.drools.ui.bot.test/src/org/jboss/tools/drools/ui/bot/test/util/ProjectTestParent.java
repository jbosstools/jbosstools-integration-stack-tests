package org.jboss.tools.drools.ui.bot.test.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.junit.Assert;

public class ProjectTestParent extends TestParent {

	protected static final String[] CONVERT_TO_DROOLS_PROJECT_PATH = {"Configure", "Convert to Drools Project"};
	protected static final String DROOLS_LIBRARY_NAME = "Drools Library";
	protected static final String DROOLS_CORE_JAR_PREFIX = "drools-core";

	private static final String DEBUG_REGEX = "(SLF4J: .*[\r\n]+)+?(.*[\r\n]+)*?";
	protected static final String HELLO_WORLD_REGEX = DEBUG_REGEX + "Hello World[\r\n]+";
	protected static final String HELLO_GOODBYE_WORLD_REGEX = HELLO_WORLD_REGEX + "Goodbye cruel world[\r\n]+";

	protected void assertNoErrorsInProblemsView() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		AbstractWait.sleep(TimePeriod.SHORT);
		Assert.assertEquals("There are errors in problems view.", 0, problemsView.getProblems(ProblemType.ERROR).size());
	}

	protected void assertConsoleText(String consoleText, String expectedRegex) {
		Assert.assertNotNull("Console text is empty.", consoleText);
		Assert.assertTrue("Unexpected text in console:\n" + consoleText, consoleText.matches(expectedRegex));
	}

	protected String getRuntimeLocation(String runtimeHome) throws UnsupportedEncodingException {
		return new File(URLDecoder.decode(runtimeHome, "utf-8")).getPath();
	}
}
