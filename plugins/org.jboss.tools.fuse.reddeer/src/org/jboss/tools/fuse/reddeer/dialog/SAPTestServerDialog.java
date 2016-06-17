package org.jboss.tools.fuse.reddeer.dialog;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ProgressInformationShellIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class SAPTestServerDialog {

	public static final String TITLE = "Test Server Connection";

	public SAPTestServerDialog activate() {
		new DefaultShell(TITLE);
		return this;
	}

	public void start() {
		activate();
		new PushButton("Start").click();
		new WaitUntil(new ProgressInformationShellIsActive(), TimePeriod.NORMAL, false);
		new WaitWhile(new ProgressInformationShellIsActive(), TimePeriod.LONG);
	}

	public void stop() {
		activate();
		new PushButton("Stop").click();
	}

	public void clear() {
		activate();
		new PushButton("Clear").click();
	}

	public void close() {
		activate();
		new PushButton("Close").click();
		new WaitWhile(new ShellWithTextIsAvailable(TITLE));
	}

	public Text getResultText() {
		activate();
		return new DefaultText();
	}
}