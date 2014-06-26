package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * JCA binding page
 * 
 * @author apodhrad
 * 
 */
public class JCABindingPage extends OperationOptionsPage<JMSBindingPage> {

	public static final String RESOURCE_ADAPTER_TYPE = "Resource Adapter Type:";
	public static final String RESOURCE_ADAPTER_ARCHIVE = "Resource Adapter Archive*";
	public static final String GENERIC_RESOURCE_ADAPTER = "Generic Resource Adapter";
	public static final String ENDPOINT_MAPPING_TYPE = "Endpoint Mapping Type";
	public static final String JMS_ENDPOINT = "JMS Endpoint";
	public static final String CCI_ENDPOINT = "CCI Endpoint";

	public JCABindingPage selectGenericResourceAdapter() {
		setResourceAdapterType("Generic Resource Adapter");
		return this;
	}

	public boolean isGenericResourceAdapter() {
		return getResourcseAdapterType().equals(GENERIC_RESOURCE_ADAPTER);
	}

	public JCABindingPage selectHornetQQueueResourceAdapter() {
		setResourceAdapterType("HornetQ Queue Resource Adapter");
		return this;
	}

	public JCABindingPage setResourceAdapterType(String adapterType) {
		new LabeledCombo(RESOURCE_ADAPTER_TYPE).setSelection(adapterType);
		return this;
	}

	public String getResourcseAdapterType() {
		return new LabeledCombo(RESOURCE_ADAPTER_TYPE).getSelection();
	}

	public JCABindingPage setResourceAdapterArchive(String archive) {
		new LabeledText(RESOURCE_ADAPTER_ARCHIVE).setFocus();
		new LabeledText(RESOURCE_ADAPTER_ARCHIVE).setText(archive);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getResourceAdapterArchive() {
		return new LabeledText(RESOURCE_ADAPTER_ARCHIVE).getText();
	}

	public JCABindingPage selectJMSEndpoint() {
		return setEndpointMappingType(JMS_ENDPOINT);
	}

	public JCABindingPage setEndpointMappingType(String type) {
		new LabeledCombo(ENDPOINT_MAPPING_TYPE).setSelection(type);
		return this;
	}

	public String getEndpointMappingType() {
		return new LabeledCombo(ENDPOINT_MAPPING_TYPE).getSelection();
	}
}
