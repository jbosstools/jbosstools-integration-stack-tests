package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class HTTPBindingPage extends OperationOptionsPage<HTTPBindingPage> {

	public static final String AUTHENTICATION_TYPE_BASIC = "Basic";
	public static final String AUTHENTICATION_TYPE_ = "NTLM";

	public LabeledText getContextPath() {
		return new LabeledText("Context path:");
	}

	public Text getExcludeNamespaces() {
		return new LabeledText(new DefaultGroup("Regular Expressions"), "Exclude Namespaces");
	}

	public Text getIncludeNamespaces() {
		return new LabeledText(new DefaultGroup("Regular Expressions"), "Include Namespaces");
	}

	public Text getExcludes() {
		return new LabeledText(new DefaultGroup("Regular Expressions"), "Excludes");
	}

	public Text getIncludes() {
		return new LabeledText(new DefaultGroup("Regular Expressions"), "Includes");
	}

	public Button getBrowse() {
		return new PushButton("Browse...");
	}

	public Text getPassword() {
		return new LabeledText("Password");
	}

	public Text getUserName() {
		return new LabeledText("User Name");
	}

	public Text getPort() {
		return new LabeledText("Port");
	}

	public Text getHost() {
		return new LabeledText("Host");
	}

	public Text getDomain() {
		return new LabeledText("Domain");
	}

	public Text getRealm() {
		return new LabeledText("Realm");
	}

	public Text getUser() {
		return new LabeledText("User");
	}

	public Combo getAuthenticationType() {
		return new LabeledCombo("Authentication Type");
	}

	public Text getRequestTimeout() {
		return new LabeledText("Request Timeout");
	}

	public Text getContentType() {
		return new LabeledText("Content Type");
	}

	public Combo getMethod() {
		return new LabeledCombo("Method");
	}

	public Text getAddress() {
		return new LabeledText("Address");
	}
}
