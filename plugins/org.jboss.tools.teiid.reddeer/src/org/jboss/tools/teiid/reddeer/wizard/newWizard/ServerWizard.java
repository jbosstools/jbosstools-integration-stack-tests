package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import java.util.Arrays;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerWizard extends NewMenuWizard {

	public static final String DIALOG_TITLE = "New Server";
	
	public static final String LOCAL_SERVER = "Local";
	public static final String REMOTE_SERVER = "Remote";
	public static final String FILESYSTEM_OPERATIONS = "Filesystem and shell operations";
	public static final String MANAGEMENT_OPERATIONS = "Management Operations";
	
	private ServerWizard() {
		super(DIALOG_TITLE, "Server", "Server");
		log.info("New server wizard is opened");
	}
	
	public static ServerWizard getInstance(){
		return new ServerWizard();
	}
	
	public static ServerWizard openWizard(){
		ServerWizard wizard = new ServerWizard();
		wizard.open();
		return wizard;
	}

	public ServerWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}
	
	public ServerWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	/**
	 * @param controlledBy - ServerWizard.LOCAL_SERVER or ServerWizard.REMOTE_SERVER
	 */
	public ServerWizard setTypeServer(String type) {
		activate();
		log.info("Set type server to '" + type + "'");
		new RadioButton(type).click();
		return this;
	}
	
	/**
	 * @param controlledBy - ServerWizard.FILESYSTEM_OPERATIONS or ServerWizard.MANAGEMENT_OPERATIONS
	 */
	public ServerWizard setControlled(String controlledBy) {
		activate();
		log.info("Set controled by to '" + controlledBy + "'");
		new RadioButton(controlledBy).click();
		return this;
	}
	
	public ServerWizard externallyManaged(boolean checked) {
		activate();
		log.info("Externally managed is '" + checked + "'");
		CheckBox checkBox = new CheckBox("Server lifecycle is externally managed.");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ServerWizard assignRuntime(boolean checked) {
		activate();
		log.info("Assigne runtime is '" + checked + "'");
		CheckBox checkBox = new CheckBox("Assign a runtime to this server");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	/**
	 * @param type of server for example: {"Red Hat JBoss Middleware","Red Hat JBoss Enterprise Application Platform 6.1+"}
	 */
	public ServerWizard setType(String[] type) {
		activate();
		log.info("Set server type to '" + Arrays.toString(type) + "'");
		selectType(type);
		return this;
	}
	
	public ServerWizard setName(String name) {
		activate();
		log.info("Set server name to '" + name + "'");
		new LabeledText("Server name:").setText(name);
		return this;
	}

	public ServerWizard setHost(String nameHost) {
		activate();
		log.info("Set server host name to '" + nameHost + "'");
		new DefaultCombo(0).setSelection(nameHost);
		return this;
	}
	
	public ServerWizard setPathToServer(String path) {
		activate();
		log.info("Set path to server to '" + path + "'");
		new DefaultText(0).setText(path);
		return this;
	}
	
	private void selectType(String[] type) {
		String[] array = new String[type.length];
		System.arraycopy(type, 0, array, 0, array.length);

		try {

			new DefaultTreeItem(new DefaultTree(0), array).select();// eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		try {
			array[array.length - 1] = type[array.length - 1] + "+";
			new DefaultTreeItem(new DefaultTree(0), array).select();// eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
