package org.jboss.tools.switchyard.reddeer.shell;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

/**
 * 
 * @author apodhrad
 *
 */
public class DomainPropertiesFileShell {

	public static final String TITLE = "Domain Properties File";

	public DomainPropertiesFileShell activate() {
		new DefaultShell(TITLE);
		return this;
	}

	public DomainPropertiesFileShell selectProjectClasspath() {
		new RadioButton(new DefaultGroup("Location Options"), "Project Classpath");
		return this;
	}

	public DomainPropertiesFileShell selectPublicURL() {
		new RadioButton(new DefaultGroup("Location Options"), "Public URL").click();
		return this;
	}

	public Text getProjectClasspath() {
		selectProjectClasspath();
		return new DefaultText(0);
	}

	public Text getPublicURL() {
		selectPublicURL();
		return new DefaultText(1);
	}

	public void ok() {
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable(TITLE));
	}

	public void cancel() {
		new PushButton("Cancel").click();
		new WaitWhile(new ShellIsAvailable(TITLE));
	}

}
