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

	public static final String MODEL_FOLDER = "Model folder: ";
	public static final String MODEL_NAME = "Model name: ";
	public static final String MODEL_TYPE = "Model type: ";
	public static final String TEIID_WIZARD = "DDL File (Teiid) >> Source or View Model";
	public static final String CUSTOM_WIZARD = "DDL File (General) >> Source or View Model";
	public static final String Source_Type = "Source Model";
	public static final String View_Type = "View Model";
	
	private String type;
	private String modelType;
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
	
	/**
	 * Constructor
	 * @param type - Select type of wizard (TEIID_WIZARD or CUSTOM_WIZARD)
	 */
	public DDLImportWizard(String type) {
		super(type);
		this.type=type;
	}

	public void setDdlPath(String ddlPath) {
		this.ddlPath = ddlPath;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	
	public void setAutoselectDialect(boolean autoselectDialect) {
		this.autoselectDialect = autoselectDialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void execute() {
		open();
		if(TEIID_WIZARD.equals(type)){
			new WaitUntil(new ShellWithTextIsAvailable("Import Teiid DDL"));
			new DefaultShell("Import Teiid DDL");
			new DefaultCombo(0).setText(ddlPath);
		}else if(CUSTOM_WIZARD.equals(type)){
			new WaitUntil(new ShellWithTextIsAvailable("Import DDL"));
			new DefaultShell("Import DDL");
			new DefaultCombo(0).setText(ddlPath);
			if (!autoselectDialect) { //default auto-select is clicked
				if (dialect != null) {
					// click on combo
					new DefaultCombo(1).setSelection(dialect);
				}
			}
		}
		new SWTWorkbenchBot().textWithLabel("Model name: ").setText(modelName);
		if(modelType != null){
			if(TEIID_WIZARD.equals(type)){
				new DefaultCombo(1).setText(modelType);
			}else if(CUSTOM_WIZARD.equals(type)){
				new DefaultCombo(2).setText(modelType);
			}
		}
		next();
		AbstractWait.sleep(TimePeriod.SHORT);
		if(TEIID_WIZARD.equals(type)){
			new DefaultShell("Import Teiid DDL");
		}else if(CUSTOM_WIZARD.equals(type)){
			new DefaultShell("Import DDL");
		}
		finish();
	}

}
