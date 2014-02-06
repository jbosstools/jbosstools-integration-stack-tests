package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.matcher.AllMatcher;

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
	public void execute() {
		open();
		new LabeledText(WSDL_PATH).setText(wsdl);
		next();
		AllMatcher m = new AllMatcher();
		new SWTBot().widgets(m);
		
		/*for (int i = 0; i < new SWTBot().widgets(m).size(); i++){
			System.out.println(new SWTBot().widgets(m).get(i).getClass().toString());
		}*/
		//new DefaultCombo(END_POINT).setSelection(endPoint);
		finish();
	}

}
