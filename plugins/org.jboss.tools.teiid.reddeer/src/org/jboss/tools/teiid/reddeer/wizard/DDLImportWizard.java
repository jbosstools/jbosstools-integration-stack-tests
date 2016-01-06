package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * Wizard for importing relational model from DDL
 * 
 * @author apodhrad
 * 
 */
public class DDLImportWizard extends TeiidImportWizard {

	public static final String DDL_FILE = "DDL file: ";
	public static final String MODEL_FOLDER = "Model folder: ";
	public static final String MODEL_NAME = "Model name: ";
	public static final String MODEL_TYPE = "Model type: ";

	private String ddlPath;
	private String modelName;
	private String dialect;
	private boolean autoselectDialect = false;

	// dialects
	public static final String TEIID = "TEIID";
	public static final String SQL92 = "SQL92";
	public static final String ORACLE = "ORACLE";
	public static final String POSTGRES = "POSTGRES";
	public static final String DERBY = "DERBY";

	public void setAutoselectDialect(boolean autoselectDialect) {
		this.autoselectDialect = autoselectDialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public DDLImportWizard() {
		super("DDL File >> Source or View Model");
	}

	public void setDdlPath(String ddlPath) {
		this.ddlPath = ddlPath;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void execute() {
		open();
		new WaitUntil(new ShellWithTextIsAvailable("Import DDL"));
		new DefaultShell("Import DDL");
		new DefaultCombo(0).setText(ddlPath);
		if (autoselectDialect) {
			// click on autoselect
			new SWTWorkbenchBot().checkBox("Auto-select").click();
		} else {
			if (dialect != null) {
				// click on combo
				new DefaultCombo(1).setSelection(dialect);
			}
		}
		// TODO: LabeledText
		// new LabeledText(MODEL_NAME).setText(modelName);
		new SWTWorkbenchBot().textWithLabel(MODEL_NAME).setText(modelName);

		next();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell("Import DDL");
		finish();
	}

}
