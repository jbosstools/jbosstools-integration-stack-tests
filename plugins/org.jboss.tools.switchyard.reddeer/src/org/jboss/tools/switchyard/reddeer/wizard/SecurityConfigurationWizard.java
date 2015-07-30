package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.condition.IsButtonEnabled;

public class SecurityConfigurationWizard extends WizardDialog {

	public static final String TITLE = "Security Configuration";

	public static final String NAME = "Name (Optional)";
	public static final String ROLES_ALLOWED = "Roles Allowed";
	public static final String RUN_AS = "Run As";
	public static final String SECURITY_DOMAIN = "Security Domain";
	public static final String CALLBACK_HANDLER = "Callback Handler Class";

	public SecurityConfigurationWizard() {
		super();
	}

	public void activate() {
		new DefaultShell(TITLE);
	}

	public void setName(String name) {
		activate();
		new LabeledText(NAME).typeText(name);
		new LabeledText(NAME).setText(name);
		new WaitUntil(new IsButtonEnabled("Finish", ROLES_ALLOWED, NAME), TimePeriod.LONG);
	}

	public String getName() {
		activate();
		return new LabeledText(NAME).getText();
	}

	public void setRolesAllowed(String roles) {
		activate();
		new LabeledText(ROLES_ALLOWED).setFocus();
		new LabeledText(ROLES_ALLOWED).setText(roles);
	}

	public String getRolesAllowed() {
		activate();
		return new LabeledText(ROLES_ALLOWED).getText();
	}

	public void setRunAs(String role) {
		activate();
		new LabeledText(RUN_AS).setFocus();
		new LabeledText(RUN_AS).setText(role);
	}

	public String getRunAs() {
		activate();
		return new LabeledText(RUN_AS).getText();
	}

	public void setSecurityDomain(String securityDomain) {
		activate();
		new LabeledText(SECURITY_DOMAIN).setFocus();
		new LabeledText(SECURITY_DOMAIN).setText(securityDomain);
	}

	public String getSecurityDomain() {
		activate();
		return new LabeledText(SECURITY_DOMAIN).getText();
	}

	public void selectCallbackHandlerClass(String className) {
		activate();
		log.info("Open class browser");
		new PushButton("Browse...").click();
		new DefaultShell("");
		log.info("Select callback handler to '" + className + "'");
		new DefaultText().setText(className);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new DefaultTable().select(0);
		log.info("Close class browser");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable(""));
		activate();
	}

	public String getCallbackHandlerClass() {
		activate();
		return new LabeledText(CALLBACK_HANDLER).getText();
	}

	@Override
	public void finish() {
		new WaitUntil(new IsButtonEnabled("Finish", NAME, ROLES_ALLOWED, RUN_AS, SECURITY_DOMAIN));
		super.finish();
	}
	
	
}
