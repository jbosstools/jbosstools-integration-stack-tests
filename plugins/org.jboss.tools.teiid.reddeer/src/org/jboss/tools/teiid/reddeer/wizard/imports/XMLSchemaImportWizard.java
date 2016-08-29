package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.common.wait.WaitUntil;

/**
 * Wizard for importing XML schemas
 * 
 * @author lfabriko, apodhrad
 * 
 */
public class XMLSchemaImportWizard extends TeiidImportWizard {

	private static final String IMPORT_XML_SCHEMA_SHELL = "Import XML Schema Files";

	public static final String LOCAL_IMPORT_MODE = "Import XML schemas from file system";
	public static final String REMOTE_IMPORT_MODE = "Import XML schemas via URL";

	private boolean isLocal;
	private String rootPath;
	// private List<String[]> elements;
	private String[] schemas;

	private String xmlSchemaURL;
	private String username;
	private String password;
	private boolean verifyHostname = true;
	private boolean addDependentSchemas = true;

	public XMLSchemaImportWizard() {
		super("XML Schemas");
	}

	public XMLSchemaImportWizard activate() {
		new DefaultShell(IMPORT_XML_SCHEMA_SHELL);
		return this;
	}

	public XMLSchemaImportWizard selectLocalImportMode() {
		return selectImportMode(LOCAL_IMPORT_MODE);
	}

	public XMLSchemaImportWizard selectRemoteImportMode() {
		return selectImportMode(REMOTE_IMPORT_MODE);
	}

	public XMLSchemaImportWizard selectImportMode(String importMode) {
		activate();
		log.info("Select import mode to '" + importMode + "'");
		new RadioButton(importMode).click();
		return this;
	}

	public XMLSchemaImportWizard setFromDirectory(String dir) {
		activate();
		log.info("Set from directory to '" + dir + "'");
		new SWTWorkbenchBot().comboBox().setFocus();
		new SWTWorkbenchBot().comboBox().setText(dir);
		KeyboardFactory.getKeyboard().type(SWT.TAB);
		return this;
	}

	public XMLSchemaImportWizard setToDirectory(String dir) {
		activate();
		log.info("Set to directory to '" + dir + "'");
		new DefaultText().setText(dir);
		return this;
	}

	public XMLSchemaImportWizard selectSchema(String... schema) {
		activate();
		for (int i = 0; i < schema.length; i++) {
			log.info("Select schema '" + schema[i] + "'");
			new DefaultTable().getItem(schema[i]).setChecked(true);
		}
		return this;
	}

	public String[] getSchemas() {
		return schemas;
	}

	public void setSchemas(String[] schemas) {
		this.schemas = schemas;
	}

	private String destination;

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	/*
	 * public void addElement(String[] path) { elements.add(path); }
	 * 
	 * public void addElement(String path) { elements.add(path.split("/")); }
	 */

	public void execute() {
		open();
		if (isLocal) {
			new DefaultShell(IMPORT_XML_SCHEMA_SHELL).setFocus();
			new RadioButton("Import XML schemas from file system").click();
			next();

			new SWTWorkbenchBot().comboBox().setText(rootPath);

			if (destination != null) {
				new DefaultText().setText(destination);
			}

			new SWTWorkbenchBot().text().setFocus();

			for (String schema : schemas) {
				new DefaultShell(IMPORT_XML_SCHEMA_SHELL).setFocus();
				new DefaultTable().getItem(schema).setChecked(true);
			}

			new DefaultShell(IMPORT_XML_SCHEMA_SHELL).setFocus();
			finish();
		} else {
			new RadioButton("Import XML schemas via URL").click();

			next();

			if (!addDependentSchemas) {
				new CheckBox(1).click();// by default add dependent schemas
			}

			new DefaultToolItem("Add XML schema URL").click();
			new WaitUntil(new ShellWithTextIsAvailable("XML Schema Url"));
			new DefaultShell("XML Schema Url").setFocus();
			new DefaultText(0).setText(xmlSchemaURL);
			if (username != null) {
				new DefaultText(1).setText(username);
			}
			if (password != null) {
				new DefaultText(2).setText(password);
			}
			if (!verifyHostname) {
				new CheckBox().click();// by default set to true
			}
			new PushButton("OK").click();
			finish();
			if (new SWTWorkbenchBot().activeShell().getText().contains("Dependent XML Schema Files Found")) {
				new PushButton("Yes").click();
			}
		}

	}

	public void setXmlSchemaURL(String xmlSchemaURL) {
		this.xmlSchemaURL = xmlSchemaURL;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerifyHostname(boolean verifyHostname) {
		this.verifyHostname = verifyHostname;
	}

	public void setAddDependentSchemas(boolean addDependentSchemas) {
		this.addDependentSchemas = addDependentSchemas;
	}

}
