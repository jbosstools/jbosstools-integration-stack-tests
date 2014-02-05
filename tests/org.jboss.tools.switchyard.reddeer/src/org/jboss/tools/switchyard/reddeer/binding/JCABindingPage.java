package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JCA binding page
 * 
 * @author apodhrad
 *
 */
public class JCABindingPage extends OperationOptionsPage<JMSBindingPage> {
	
	public static final String NAME = "Name";
	public static final String RESOURCE_ADAPTER_TYPE = "Resource Adapter Type:";
	public static final String RESOURCE_ADAPTER_ARCHIVE = "Resource Adapter Archive*";
	public static final String GENERIC_RESOURCE_ADAPTER = "Generic Resource Adapter";
	public static final String ENDPOINT_MAPPING_TYPE = "Endpoint Mapping Type";
	public static final String JMS_ENDPOINT = "JMS Endpoint";
	public static final String CCI_ENDPOINT = "CCI Endpoint";
	
	public JCABindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}
	
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
		new DefaultCombo(RESOURCE_ADAPTER_TYPE).setSelection(adapterType);
		return this;
	}
	
	public String getResourcseAdapterType() {
		return new DefaultCombo(RESOURCE_ADAPTER_TYPE).getSelection();
	}
	
	public JCABindingPage setResourceAdapterArchive(String archive) {
		new LabeledText(RESOURCE_ADAPTER_ARCHIVE).setText(archive);
		return this;
	}
	
	public String getResourceAdapterArchive() {
		return new LabeledText(RESOURCE_ADAPTER_ARCHIVE).getText();
	}
	
	public JCABindingPage selectJMSEndpoint() {
		return setEndpointMappingType(JMS_ENDPOINT);
	}
	
	public JCABindingPage setEndpointMappingType(String type) {
		new DefaultCombo(ENDPOINT_MAPPING_TYPE).setSelection(type);
		return this;
	}
	
	public String getEndpointMappingType() {
		return new DefaultCombo(ENDPOINT_MAPPING_TYPE).getSelection();
	}
}
