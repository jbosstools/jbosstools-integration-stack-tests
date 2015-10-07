package org.jboss.tools.fuse.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.runtime.reddeer.view.TerminalView;

/**
 * Checks whether Fuse server's log contains specified text
 * 
 * @author tsedmik
 */
public class FuseLogContainsText implements WaitCondition {

	private String text;

	public FuseLogContainsText(String text) {

		this.text = text;
	}

	@Override
	public boolean test() {

		return new TerminalView().execute("log:display").contains(text);
	}

	@Override
	public String description() {

		return "Fuse Server's log contains: \"" + text + "\"";
	}

}
