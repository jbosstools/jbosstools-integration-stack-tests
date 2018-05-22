package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

/**
 * Wizard for creating REST connection profile
 * 
 * @author mmakovy
 *
 */

public class RestConnectionProfileWizard extends ConnectionProfileWizard {

    public static final String DIALOG_TITLE = "New connection profile";

	public static final String AUTH_TYPE_BASIC = "HTTPBasic";
	public static final String AUTH_TYPE_DIGEST = "Digest";
	
	public static final String TYPE_XML = "XML";
	public static final String TYPE_JSON = "JSON";

	public RestConnectionProfileWizard(String name) {
		super("Web Services Data Source (REST)",name);
		log.info("REST profile wizard is opened");
	}
	
	public static RestConnectionProfileWizard getInstance(){
		return new RestConnectionProfileWizard(null);
	}
	
	public static RestConnectionProfileWizard openWizard(String name){
		RestConnectionProfileWizard wizard = new RestConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}
	
	public RestConnectionProfileWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public RestConnectionProfileWizard setConnectionUrl(String url){
		log.info("Set connection url to : '" + url + "'");
		activateWizard();
		new LabeledText(new DefaultGroup("Properties"), "Connection URL").setText(url);
		return this;
	}
	
	/**
	 * @param type use one of type (RestProfileWizard.TYPE_XML or RestProfileWizard.TYPE_JSON)
	 */
	public RestConnectionProfileWizard setType(String type){
		log.info("Set type to : '" + type + "'");
		activateWizard();
		new DefaultCombo().setSelection(type);
		return this;
	}
	
	/**
	 * @param authType use one of type (RestConnectionProfileWizard.AUTH_TYPE_BASIC or RestConnectionProfileWizard.AUTH_TYPE_DIGEST)
	 */
	public RestConnectionProfileWizard setAuth(String authType,String userName, String password){
		log.info("Set authType to : '" + authType + "', username: '" + userName +"', password: '" + password +"'");
		activateWizard();
		new DefaultCombo(1).setSelection(authType);
		new LabeledText(new DefaultGroup("Properties"),"User Name").setText(userName);
		new LabeledText(new DefaultGroup("Properties"),"Password").setText(password);

		return this;
	}
	
	@Override
	public RestConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activateWizard();
		return this;
	}
}
