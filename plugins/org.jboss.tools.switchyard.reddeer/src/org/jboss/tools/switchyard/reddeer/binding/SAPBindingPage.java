package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SAP binding page
 * 
 * @author apodhrad
 * 
 */
public class SAPBindingPage extends OperationOptionsPage<SAPBindingPage> {

	public static final String SAP_OBJECT_IDOC_LIST = "IDoc List";
	public static final String SAP_OBJECT_SRFC = "sRFC";
	public static final String SAP_OBJECT_TRFC = "tRFC";

	public LabeledText getRFCName() {
		return new LabeledText("RFC Name*");
	}

	public LabeledText getServer() {
		return new LabeledText("Server*");
	}

	public Text getApplicationRelease() {
		return new LabeledText("Application Release");
	}

	public Text getSystemRelease() {
		return new LabeledText("System Release");
	}

	public Text getIDocTypeExtension() {
		return new LabeledText("IDoc Type Extension");
	}

	public Text getIDocType() {
		return new LabeledText("IDoc Type*");
	}

	public Combo getSAPObject() {
		return new LabeledCombo("SAP Object");
	}

}
