package org.jboss.tools.esb.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBProviderWizard extends WizardDialog {

	public static final String NAME = "Name:*";
	public static final String CHANNEL = "Channel ID:*";
	public static final String HOSTNAME = "Hostname:*";
	public static final String CFG_FILE = "Hibernate Cfg File:*";
	public static final String PROTOCOL = "Protocol:*";
	public static final String HOST = "Host:*";
	public static final String PORT = "Port:*";
	public static final String CONNECTION_FACTORY = "Connection Factory:*";
	public static final String SCHEDULE = "Schedule ID:*";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public ESBProviderWizard(String provider) {
		super();
		new DefaultShell("Add " + provider);
	}

	public void setName(String name) {
		// new LabeledText(NAME).setFocus();
		// new LabeledText(NAME).setText(name);
		bot.textWithLabel(NAME).setText(name);
	}

	public void setChannel(String channel) {
		// new LabeledText(CHANNEL).setFocus();
		// new LabeledText(CHANNEL).setText(channel);
		bot.textWithLabel(CHANNEL).setText(channel);
	}

	public void setHostname(String hostname) {
		// new LabeledText(HOSTNAME).setFocus();
		// new LabeledText(HOSTNAME).setText(hostname);
		bot.textWithLabel(HOSTNAME).setText(hostname);
	}

	public void setCfgFile(String cfgFile) {
		// new LabeledText(CFG_FILE).setFocus();
		// new LabeledText(CFG_FILE).setText(cfgFile);
		bot.textWithLabel(CFG_FILE).setText(cfgFile);
	}

	public void setClassName(String className) {
		new DefaultText(0).setFocus();
		new DefaultText(0).setText(className);
	}

	public void setEvent(String event) {
		new DefaultText(1).setFocus();
		new DefaultText(1).setText(event);
	}

	public void setProtocol(String protocol) {
		new DefaultCombo().setSelection(protocol);
	}

	public void setHost(String host) {
		bot.textWithLabel(HOST).setText(host);
	}

	public void setPort(String port) {
		bot.textWithLabel(PORT).setText(port);
	}

	public void setConnectionFactory(String connectionFactory) {
		new DefaultCombo().setSelection(connectionFactory);
	}
	
	public void setSchedule(String type, String schedule) {
		new RadioButton(type);
		bot.textWithLabel(SCHEDULE).setText(schedule);
	}
}
