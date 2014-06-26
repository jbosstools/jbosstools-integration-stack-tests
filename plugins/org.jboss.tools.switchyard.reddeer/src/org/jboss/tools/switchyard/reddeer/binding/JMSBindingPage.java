package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * JMS binding page
 * 
 * @author apodhrad
 * 
 */
public class JMSBindingPage extends OperationOptionsPage<JMSBindingPage> {

	public static final String QUEUE_TOPIC_NAME = "Queue/Topic Name*";

	public JMSBindingPage setQueueTopicName(String name) {
		new LabeledText(QUEUE_TOPIC_NAME).setFocus();
		new LabeledText(QUEUE_TOPIC_NAME).setText(name);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getQueueTopicName() {
		return new LabeledText(QUEUE_TOPIC_NAME).getText();
	}
}
