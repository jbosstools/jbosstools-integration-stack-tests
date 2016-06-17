package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from XML
 * 
 * @author apodhrad
 * 
 */
public class XMLImportWizard extends TeiidImportWizard {

	private boolean isLocal;
	private String name;
	private String profileName;
	private String rootPath;
	private List<String[]> elements;
	private String jndiName;

	public XMLImportWizard() {
		super("File Source (XML) >> Source and View Model");
		elements = new ArrayList<String[]>();
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void addElement(String[] path) {
		elements.add(path);
	}

	public void addElement(String path) {
		elements.add(path.split("/"));
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public void execute() {
		open();
		new WaitUntil(new ShellWithTextIsAvailable("Import From XML File Source"));
		new DefaultShell("Import From XML File Source").setFocus();
		if (isLocal) {
			new RadioButton("XML file on local file system").click();
		} else {
			new RadioButton("XML file via remote URL").click();
		}

		next();
		new DefaultCombo(0).setSelection(profileName);
		new LabeledText("Name:").setText(name + "Source");

		next();
		
		if(jndiName != null){
			new DefaultText(new DefaultGroup("JBoss Data Source Information"),0).setText(jndiName);
		}
		next();
		
		new SWTWorkbenchBot().text(1).setText(rootPath);
		for (String[] path : elements) {
			new DefaultTreeItem(new DefaultTree(0), path).select();
			new PushButton("Add").click();
		}

		next();
		new SWTWorkbenchBot().textWithLabel("Name:").setText(name + "View");
		new LabeledText("New view table name:").setText(name + "Table");

		finish();
	}

}
