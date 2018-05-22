package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import java.io.File;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

/**
 * Wizard for creating WSDL connection profile
 * 
 * @author apodhrad
 *
 */
public class WsdlConnectionProfileWizard extends ConnectionProfileWizard {

    public static final String DIALOG_TITLE = "New connection profile";
	
	public static final String AUTH_TYPE_BASIC = "HTTPBasic";
	public static final String AUTH_TYPE_DIGEST = "Digest";
	
	private WsdlConnectionProfileWizard(String name) {
		super("Web Services Data Source (SOAP)",name);
		log.info("WSDL profile wizard is opened");
	}
	
	public static WsdlConnectionProfileWizard getInstance(){
		return new WsdlConnectionProfileWizard(null);
	}
	
	public static WsdlConnectionProfileWizard openWizard(String name){
		WsdlConnectionProfileWizard wizard = new WsdlConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}
	
	public WsdlConnectionProfileWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public WsdlConnectionProfileWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public WsdlConnectionProfileWizard setWsdl(String wsdl) {
		log.info("Set wsdl to : '" + wsdl + "'");
		activateWizard();
		new PushButton("URL...").click();
		new DefaultShell("WSDL URL");
		
		if (!wsdl.contains("http")) {
			wsdl = "file:" + new File(wsdl).getAbsolutePath();
		}
		new LabeledText("Enter WSDL URL:").setText(wsdl);
		new PushButton("OK").click();
		activateWizard();
		return this;
	}
	
	public WsdlConnectionProfileWizard setWsdl(String wsdl, String securityType, String userName, String password) {
		log.info("Set wsdl to : '" + wsdl + "'");
		activateWizard();
		new PushButton("URL...").click();
		new DefaultShell("WSDL URL");
		
		if (!wsdl.contains("http")) {
			wsdl = "file:" + new File(wsdl).getAbsolutePath();
		}
		new LabeledText("Enter WSDL URL:").setText(wsdl);
		
		new DefaultCombo().setSelection(securityType);
		new LabeledText("User Name").setText(userName);
		new LabeledText("Password").setText(password);
		
		new PushButton("OK").click();
		activateWizard();
		return this;
	}

	public WsdlConnectionProfileWizard setEndPoint(String endPoint){
		log.info("Set end point to : '" + endPoint + "'");
		activateWizard();
		new DefaultCombo().setSelection(endPoint);
		return this;
	}
	
	@Override
	public WsdlConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		AbstractWait.sleep(TimePeriod.DEFAULT);
		new DefaultText("The WSDL has been successfully pinged and validated");
		return this;
	}
}
