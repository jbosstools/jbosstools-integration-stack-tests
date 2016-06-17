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

public class SAPTestDestinationDialog {

	public static final String TITLE = "Test Destination Connection";

	public SAPTestDestinationDialog activate() {
		new DefaultShell(TITLE);
		return this;
	}

	public String test() {
		activate();
		final String oldResult = getResultTXT().getText();
		new PushButton("Test").click();
		new WaitUntil(new ProgressInformationShellIsActive(), TimePeriod.NORMAL, false);
		new WaitWhile(new ProgressInformationShellIsActive(), TimePeriod.LONG);
		return getResultTXT().getText().replaceFirst(oldResult, "").trim();
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

	public Text getResultTXT() {
		activate();
		return new DefaultText();
	}
}