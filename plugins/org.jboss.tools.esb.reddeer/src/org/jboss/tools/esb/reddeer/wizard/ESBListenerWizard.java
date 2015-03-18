package org.jboss.tools.esb.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBListenerWizard extends WizardDialog {

	public static final String NAME = "Name:*";
	public static final String SCRIPT = "Script:*";
	public static final String ADAPTER = "Adapter:*";
	public static final String HOST = "Host:*";
	public static final String PORT = "Port:*";
	public static final String CHANNEL_REF = "Channel ID Ref:";
	public static final String ENDPOINT_CLASS = "Endpoint Class:*";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public ESBListenerWizard(String listener) {
		super();
		new DefaultShell("Add " + listener);
	}

	public void setName(String name) {
		bot.textWithLabel(NAME).setText(name);
	}

	public void setScript(String script) {
		bot.textWithLabel(SCRIPT).setText(script);
	}

	public void setAdapter(String adapter) {
		bot.textWithLabel(ADAPTER).setText(adapter);
	}

	public void setHost(String host) {
		bot.textWithLabel(HOST).setText(host);
	}

	public void setPort(String port) {
		bot.textWithLabel(PORT).setText(port);
	}

	public void setChannelRef(String channel) {
		new DefaultCombo().setSelection(channel);
	}

	public void setEndpointClass(String endpointClass) {
		bot.textWithLabel(ENDPOINT_CLASS).setText(endpointClass);
	}
}
