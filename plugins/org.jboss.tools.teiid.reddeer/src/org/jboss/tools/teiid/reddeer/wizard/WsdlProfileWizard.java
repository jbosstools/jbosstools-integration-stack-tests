package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Wizard for creating WSDL connection profile
 * 
 * @author apodhrad
 *
 */
public class WsdlProfileWizard extends TeiidProfileWizard {

	public static final String WSDL_PATH = "Connection URL or File Path";
	public static final String END_POINT = "End Point";

	private String wsdl;
	private String endPoint;

	public WsdlProfileWizard() {
		super("Web Services Data Source (SOAP)");
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public void execute() {// TODO URL if security not default -- this was also somewhere in web service model
		open();
		new SWTWorkbenchBot().textWithLabel(WSDL_PATH).setText(wsdl);
		next();
		new DefaultCombo().setSelection(endPoint);
		finish();
	}

}
