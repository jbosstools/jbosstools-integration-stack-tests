package org.jboss.tools.teiid.reddeer.condition;

import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.lookup.ShellLookup;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;

public class IsPreviewInProgress implements WaitCondition {
	private static final Logger log = Logger.getLogger(IsPreviewInProgress.class);
	private static final Matcher<String> matcher = new RegexMatcher("Preview.*");
	
	@Override
	public boolean test() {
		log.debug("Waiting while preview is in progress");
		Shell shell = ShellLookup.getInstance().getShell(matcher, TimePeriod.NONE);
		return shell != null;
	}

	@Override
	public String description() {
		return "shell with title matching " + matcher + " is available";
	}
}
