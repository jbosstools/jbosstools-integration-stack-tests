package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New Server";
	
	public static final String LOCAL_SERVER = "Local";
	public static final String REMOTE_SERVER = "Remote";
	public static final String FILESYSTEM_OPERATIONS = "Filesystem and shell operations";
	public static final String MANAGEMENT_OPERATIONS = "Management Operations";
	
	public ServerWizard() {
		super("Server", "Server");
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
		new RadioButton(type).click();
		return this;
	}
	
	/**
	 * @param controlledBy - ServerWizard.FILESYSTEM_OPERATIONS or ServerWizard.MANAGEMENT_OPERATIONS
	 */
	public ServerWizard setControlled(String controlledBy) {
		activate();
		new RadioButton(controlledBy).click();
		return this;
	}
	
	public ServerWizard externallyManaged(boolean externallyManaged) {
		activate();
		new CheckBox("Server is externally managed. Assume server is started. ").toggle(externallyManaged);
		return this;
	}
	
	public ServerWizard assignRuntime(boolean externallyManaged) {
		activate();
		new CheckBox("Assign a runtime to this server").toggle(externallyManaged);
		return this;
	}
	
	/**
	 * @param type of server for example: {"Red Hat JBoss Middleware","Red Hat JBoss Enterprise Application Platform 6.1+"}
	 */
	public ServerWizard setType(String[] type) {
		activate();
		selectType(type);
		return this;
	}
	
	public ServerWizard setName(String name) {
		activate();
		new LabeledText("Server name:").setText(name);
		return this;
	}

	public ServerWizard setHost(String nameHost) {
		activate();
		new DefaultCombo(0).setSelection(nameHost);
		return this;
	}
	
	public ServerWizard setPathToServer(String path) {
		activate();
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
