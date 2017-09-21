package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Netty TCP binding page
 * 
 * @author apodhrad
 * 
 */
public class NettyTCPBindingPage extends OperationOptionsPage<NettyTCPBindingPage> {

	public LabeledText getPort() {
		return new LabeledText("Port*");
	}

	public LabeledText getHost() {
		return new LabeledText("Host*");
	}

}
