package org.jboss.tools.drools.reddeer.dialog;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;

public class DroolsRuntimeDialog {

	private static final String NAME_LABEL = "Name:";
	private static final String PATH_LABEL = "Path:";
	private static final String VERSION_LABEL = "Version:";

	public void setName(String name) {
		new LabeledText(NAME_LABEL).setText(name);
	}

	public void setLocation(String location) {
		new LabeledText(PATH_LABEL).setText(location);
	}

	public void setVersion(String version) {
		new LabeledText(VERSION_LABEL).setText(version);
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}

	public void ok() {
		new LabeledText("Name:").setFocus();
		AbstractWait.sleep(TimePeriod.SHORT);
		new LabeledText("Path:").setFocus();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("OK").click();
	}

	public boolean isValid() {
		return new PushButton("OK").isEnabled();
	}
	
	public String getWarningText() {
		return new DefaultText(3).getText();
	}
}
