package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class CreateWarDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(CreateWarDialog.class);
	
	private boolean soap;
	
	/** 
	 * @param xmlDialog - used to set dialog's title, because it has different titles 
	 * 		when creating WS from XmlModel and RelationalModel
	 */
	public CreateWarDialog(boolean soap) {
		super((soap) ? "Create Web Service WAR File" : "Create REST WAR File");
		this.soap = soap;
	}
	
	public static CreateWarDialog getSoapInstance(){
		return new CreateWarDialog(true);
	}
	
	public static CreateWarDialog getRestInstance(){
		return new CreateWarDialog(false);
	}
		
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		if (new ShellWithTextIsActive("Overwrite existing WAR file?").test()){
			new YesButton().click();
			new WaitWhile(new ShellWithTextIsActive("Overwrite existing WAR file?"), TimePeriod.NORMAL);
			AbstractWait.sleep(TimePeriod.SHORT);
		}
		new DefaultShell(soap ? "Web Service WAR File Created" : "REST WAR File Created");
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Sets context name.
	 */
	public CreateWarDialog setContextName(String name) {
		log.info("Setting context name to '" + name + "'");
		new LabeledText("Context Name:").setText(name);
		activate();
		return this;
	}
	
	/**
	 * Sets VDB JNDI name.
	 */
	public CreateWarDialog setVdbJndiName(String name) {
		log.info("Setting VDB JNDI name to '" + name + "'");
		new LabeledText("VDB JNDI Name:").setText(name);
		activate();
		return this;
	}
	
	/**
	 * Sets location where new WAR file will be created.
	 * @param path - absolute path to directory (/.../<DIR>)
	 */
	public CreateWarDialog setWarFileLocation(String path) {
		log.info("Setting WAR file location to '" + Arrays.toString(path.split("/")) + "'");
		new LabeledText(soap ? "WAR File Save Location:" : "REST WAR File Save Location:").setText(path);
		activate();
		return this;
	}
	
	/**
	 * Sets security to HTTPBasic with specified realm and role.
	 */
	public CreateWarDialog setHttpBasicSecurity(String realm, String role) {
		log.info("Setting HTTP Basic Security with realm '" + realm + "' and role '" + role + "'");
		new RadioButton("HTTPBasic").click();
		if (soap){
			new DefaultTabItem("HTTPBasic Options").activate();
		} 
		new LabeledText("Realm:").setText(realm);
		new LabeledText("Role:").setText(role);
		if (soap){
			new DefaultTabItem("General").activate();
		} 
		activate();
		return this;
	}
	
	/**
	 * Sets security to None.
	 */
	public CreateWarDialog setNoneSecurity() {
		log.info("Setting security to None");
		new RadioButton("None").click();
		activate();
		return this;
	}

}
