package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Netty UDP binding page
 * 
 * @author apodhrad
 * 
 */
public class NettyUDPBindingPage extends OperationOptionsPage<NettyUDPBindingPage> {

	public LabeledText getPort() {
		return new LabeledText("Port*");
	}

	public LabeledText getHost() {
		return new LabeledText("Host*");
	}

	public CheckBox getBroadcast() {
		return new CheckBox("Broadcast");
	}

}
