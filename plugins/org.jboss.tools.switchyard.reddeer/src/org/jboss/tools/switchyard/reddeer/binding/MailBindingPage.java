package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Mail binding page
 * 
 * @author apodhrad
 * 
 */
public class MailBindingPage extends OperationOptionsPage<MailBindingPage> {

	public static final String ACCOUNT_TYPE_IMAP = "imap";
	public static final String ACCOUNT_TYPE_POP3 = "pop3";

	public LabeledText getFetchSize() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Fetch Size");
	}

	public LabeledText getFolderName() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Folder Name");
	}

	public LabeledText getPassword() {
		return new LabeledText("Password");
	}

	public LabeledText getUserName() {
		return new LabeledText("User Name");
	}

	public LabeledText getPort() {
		return new LabeledText("Port");
	}

	public LabeledText getHost() {
		return new LabeledText("Host*");
	}

	public LabeledCombo getAccountType() {
		return new LabeledCombo(new DefaultGroup("Consumer Options"), "Account Type");
	}

	public CheckBox getDelete() {
		return new CheckBox(new DefaultGroup("Consumer Options"), "Delete");
	}

	public CheckBox getUnreadOnly() {
		return new CheckBox(new DefaultGroup("Consumer Options"), "Unread Only");
	}

	public CheckBox getSecured() {
		return new CheckBox("Secured");
	}

	public Text getReplyTo() {
		return new LabeledText(new DefaultGroup("Producer Options"), "Reply To");
	}

	public Text getBCC() {
		return new LabeledText(new DefaultGroup("Producer Options"), "BCC");
	}

	public Text getCC() {
		return new LabeledText(new DefaultGroup("Producer Options"), "CC");
	}

	public Text getTo() {
		return new LabeledText(new DefaultGroup("Producer Options"), "To");
	}

	public Text getFrom() {
		return new LabeledText(new DefaultGroup("Producer Options"), "From");
	}

	public Text getSubject() {
		return new LabeledText(new DefaultGroup("Producer Options"), "Subject");
	}

}
