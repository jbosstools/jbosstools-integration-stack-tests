package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.LabeledTextExt;

/**
 * JMS binding page
 * 
 * @author apodhrad
 * 
 */
public class JMSBindingPage extends OperationOptionsPage<JMSBindingPage> {

	public static final String TYPE_QUEUE = "Queue";
	public static final String TYPE_TOPIC = "Topic";

	public static final String QUEUE_TOPIC_NAME = "Queue/Topic Name*";

	public JMSBindingPage setQueueTopicName(String name) {
		new LabeledTextExt(QUEUE_TOPIC_NAME).setFocus();
		new LabeledTextExt(QUEUE_TOPIC_NAME).setText(name);
		return this;
	}

	public String getQueueTopicName() {
		return new LabeledTextExt(QUEUE_TOPIC_NAME).getText();
	}

	public LabeledText getTransactionManager() {
		return new LabeledText("Transaction Manager");
	}

	public LabeledText getSelector() {
		return new LabeledText("Selector");
	}

	public LabeledText getReplyTo() {
		return new LabeledText("Reply To");
	}

	public LabeledText getMaximumConcurrentConsumers() {
		return new LabeledText("Maximum Concurrent Consumers");
	}

	public LabeledText getConcurrentConsumers() {
		return new LabeledText("Concurrent Consumers");
	}

	public LabeledText getConnectionFactory() {
		return new LabeledText("Connection Factory*");
	}

	public LabeledCombo getType() {
		return new LabeledCombo("Type");
	}

	public CheckBox getTransacted() {
		return new CheckBox("Transacted");
	}

}
