package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * MQTT binding page
 * 
 * @author apodhrad
 * 
 */
public class MQTTBindingPage extends OperationOptionsPage<MQTTBindingPage> {
	
	public static final String QOS_AT_LEAST_ONCE = "AtLeastOnce";
	public static final String QOS_AT_MOST_ONCE = "AtMostOnce";
	public static final String QOS_EXACTLY_ONCE = "ExactlyOnce";

	public LabeledText getReconnectAttemptsMax() {
		return new LabeledText("Reconnect Attempts Max");
	}

	public LabeledText getConnectAttemptsMax() {
		return new LabeledText("Connect Attempts Max");
	}

	public LabeledText getSubscribeTopicName() {
		return new LabeledText("Subscribe Topic Name");
	}

	public LabeledText getPublishTopicName() {
		return new LabeledText("Publish Topic Name");
	}
	
	public LabeledText getHostURI() {
		return new LabeledText("Host URI (Default: tcp://127.0.0.1:1883)");
	}

	public LabeledCombo getQualityofService() {
		return new LabeledCombo("Quality of Service");
	}
}
