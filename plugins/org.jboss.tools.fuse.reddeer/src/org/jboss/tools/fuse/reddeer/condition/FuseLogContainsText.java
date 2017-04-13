package org.jboss.tools.fuse.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.runtime.reddeer.utils.FuseShellSSH;

/**
 * Checks whether Fuse server's log contains specified text
 * 
 * @author tsedmik
 */
public class FuseLogContainsText extends AbstractWaitCondition {

	private String text;

	public FuseLogContainsText(String text) {

		this.text = text;
	}

	@Override
	public boolean test() {

		return new FuseShellSSH().execute("log:display").contains(text);
	}

	@Override
	public String description() {

		return "Fuse Server's log contains: \"" + text + "\"";
	}

}
