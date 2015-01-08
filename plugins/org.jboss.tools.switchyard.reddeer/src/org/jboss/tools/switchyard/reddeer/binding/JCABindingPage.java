package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JCA binding page
 * 
 * @author apodhrad
 * 
 */
public class JCABindingPage extends OperationOptionsPage<JMSBindingPage> {
	
	public static final String RESOURCE_ADAPTER_GENERIC = "Generic Resource Adapter";
	public static final String RESOURCE_ADAPTER_HORNETQ_QUEUE = "HornetQ Queue Resource Adapter";
	public static final String RESOURCE_ADAPTER_HORNETQ_TOPIC = "HornetQ Topic Resource Adapter";
	
	public static final String ENDPOINT_JMS = "JMS Endpoint";
	public static final String ENDPOINT_CCI = "CCI Endpoint";
	public static final String ENDPOINT_CUSTOM = "Custom";
	
	public static final String ACKNOWLEDGE_MODE_AUTO = "Auto-acknowledge";
	public static final String ACKNOWLEDGE_MODE_DUPS_OK = "Dups-ok-acknowledge";

	public static final String SUBSCRIPTION_DURABLE = "Durable";
	public static final String SUBSCRIPTION_NONDURABLE = "NonDurable";

	public LabeledText getListenerType() {
		return new LabeledText(new DefaultGroup("Custom JCA Endpoint"), "Listener Type");
	}

	public LabeledText getEndpointType() {
		return new LabeledText(new DefaultGroup("Custom JCA Endpoint"), "Endpoint Type");
	}

	public LabeledText getPassword() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "Password:");
	}

	public LabeledText getUserName() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "User Name:");
	}

	public LabeledText getFaultTo() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "Fault To:");
	}

	public LabeledText getReplyTo() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "Reply To:");
	}

	public LabeledText getDestinationJNDIPropertiesFileName() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "Destination JNDI Properties File Name:");
	}

	public LabeledText getJNDIPropertiesFileName() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "JNDI Properties File Name:");
	}

	public LabeledText getConnectionFactoryJNDIName() {
		return new LabeledText(new DefaultGroup("JMS Endpoint Properties"), "Connection Factory JNDI Name:");
	}

	public LabeledText getBatchTimeoutin() {
		return new LabeledText(new DefaultGroup("Batch Commit Options"), "Batch Timeout (in MS)");
	}

	public LabeledText getBatchSize() {
		return new LabeledText(new DefaultGroup("Batch Commit Options"), "Batch Size");
	}

	public LabeledText getSubscriptionName() {
		return new LabeledText("Subscription Name");
	}

	public LabeledText getClientID() {
		return new LabeledText("Client ID*");
	}

	public LabeledText getMessageSelector() {
		return new LabeledText("Message Selector");
	}

	public LabeledText getDestinationTopic() {
		return new LabeledText("Destination (Topic)*");
	}

	public LabeledText getDestinationQueue() {
		return new LabeledText("Destination (Queue)*");
	}

	public LabeledText getResourceAdapterArchive() {
		return new LabeledText("Resource Adapter Archive*");
	}

	public LabeledCombo getMessageType() {
		return new LabeledCombo(new DefaultGroup("JMS Endpoint Properties"), "Message Type:");
	}

	public LabeledCombo getDestinationType() {
		return new LabeledCombo(new DefaultGroup("JMS Endpoint Properties"), "Destination Type");
	}

	public LabeledCombo getEndpointMappingType() {
		return new LabeledCombo("Endpoint Mapping Type");
	}

	public LabeledCombo getSubscriptionDurability() {
		return new LabeledCombo("Subscription Durability");
	}

	public LabeledCombo getAcknowledgeMode() {
		return new LabeledCombo("Acknowledge Mode");
	}

	public LabeledCombo getResourceAdapterType() {
		return new LabeledCombo("Resource Adapter Type:");
	}

	public CheckBox getUseJNDILookupforDestination() {
		return new CheckBox(new DefaultGroup("JMS Endpoint Properties"), "Use JNDI Lookup for Destination");
	}

	public LabeledText getTransacted() {
		return new LabeledText("Transacted");
	}

}
