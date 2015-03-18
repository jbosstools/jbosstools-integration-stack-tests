package org.jboss.tools.switchyard.reddeer.project;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;

/**
 * Extension for project item.
 * 
 * @author apodhrad
 * 
 */
public class ProjectItemExt {

	private ProjectItem projectItem;

	public ProjectItemExt(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	@SuppressWarnings("unchecked")
	public void runAs(String menu) {
		projectItem.select();
		new ContextMenu(new WithMnemonicTextMatcher("Run As"), new MenuMatcher(menu)).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitWhile(new ConsoleHasChanged(), TimePeriod.LONG);
	}

	@SuppressWarnings("unchecked")
	public void runAs(Configuration config) {
		projectItem.select();
		new ContextMenu(new WithMnemonicTextMatcher("Run As"), config.getMatcher()).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@SuppressWarnings("unchecked")
	public void debugAs(Configuration config) {
		projectItem.select();
		new ContextMenu(new WithMnemonicTextMatcher("Debug As"), config.getMatcher()).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void runAsJUnitTest() {
		runAs("JUnit Test");
	}

	private class MenuMatcher extends BaseMatcher<String> {

		private String text;

		public MenuMatcher(String text) {
			super();
			this.text = text;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof String) {
				String label = (String) obj;
				return label.contains(text);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {
			desc.appendText("menu item containing '" + text + "'");
		}

	}

	public enum Configuration {

		JUNIT_TEST("JUnit Test");

		private Matcher<String> matcher;

		private Configuration(String config) {
			matcher = new RegexMatcher("[1-9]+ " + config + ".*");
		}

		public Matcher<String> getMatcher() {
			return matcher;
		}
	}
}
