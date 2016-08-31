package org.jboss.tools.teiid.reddeer.condition;

import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class IsPreviewInProgress extends AbstractWaitCondition {
	private static final Logger log = Logger.getLogger(IsPreviewInProgress.class);
	private static final Matcher<String> matcher = new RegexMatcher("Preview.*");

	@Override
	public boolean test() {
		log.debug("Waiting while preview is in progress");
		try {
			new DefaultShell(matcher);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String description() {
		return "shell with title matching " + matcher + " is available";
	}
}
