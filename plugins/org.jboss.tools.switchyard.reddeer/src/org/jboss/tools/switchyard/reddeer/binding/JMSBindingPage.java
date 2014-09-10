package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JMS binding page
 * 
 * @author apodhrad
 * 
 */
public class JMSBindingPage extends OperationOptionsPage<JMSBindingPage> {

	public static final String QUEUE_TOPIC_NAME = "Queue/Topic Name*";

	public JMSBindingPage setQueueTopicName(String name) {
		activate();
		// TODO Replace with RedDeer implementation
		new SWTBot().textWithLabel(QUEUE_TOPIC_NAME).setFocus();
		new SWTBot().textWithLabel(QUEUE_TOPIC_NAME).setText(name);
		return this;
	}
	
	public String getQueueTopicName() {
		return new LabeledText(QUEUE_TOPIC_NAME).getText();
	}

}
