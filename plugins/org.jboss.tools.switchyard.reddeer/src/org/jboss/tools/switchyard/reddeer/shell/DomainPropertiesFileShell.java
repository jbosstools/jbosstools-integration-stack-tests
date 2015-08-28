package org.jboss.tools.switchyard.reddeer.shell;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.WaitWhile;

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
		new WaitWhile(new ShellWithTextIsAvailable(TITLE));
	}

	public void cancel() {
		new PushButton("Cancel").click();
		new WaitWhile(new ShellWithTextIsAvailable(TITLE));
	}

}
