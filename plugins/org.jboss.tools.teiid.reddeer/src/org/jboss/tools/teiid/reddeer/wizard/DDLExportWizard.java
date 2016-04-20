package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.ExportWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for export model as DDL
 * @author mkralik
 */

public class DDLExportWizard extends ExportWizardDialog {
	
	public static final String TEIID_WIZARD = "Teiid DDL";
	public static final String CUSTOM_WIZARD = "Data Definition Language (DDL)"; //TODO not implemented yet in the execute
	
	private String type;
	private boolean nameInSource = false;
	private boolean nativeType = false;
	private String[] path;
	private String exportName = "exportDDL";
	private String[] exportLocation ;
	
	/**
	 * Constructor
	 * @param type - Select type of wizard (TEIID_WIZARD or CUSTOM_WIZARD)
	 */
	public DDLExportWizard(String type) {
		super("Teiid Designer", type);
		this.type=type;
	}
	
	public void setNameInSource(boolean nameInSource) {
		this.nameInSource = nameInSource;
	}
	public void setNativeType(boolean nativeType) {
		this.nativeType = nativeType;
	}
	public void setPathToModel(String... path) {
		this.path = path;
	}
	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public void setExportLocation(String... exportLocation) {
		this.exportLocation = exportLocation;
	}
	
	public void execute() {
		open();
		if(TEIID_WIZARD.equals(type)){
			new DefaultShell("Export Teiid DDL");
			new PushButton("...").click();
			new DefaultShell();
			new DefaultTreeItem(path).select();
			new PushButton("OK").click();
			new DefaultShell("Export Teiid DDL");
			if(nameInSource){
				new CheckBox("Add Name In Source values as OPTIONS").click();
			}
			if(nativeType){
				new CheckBox("Add Native Type values as OPTIONS").click();
			}
			next();
			new PushButton("Export to Workspace...").click();
			new DefaultShell("Export DDL To Workspace");
			new PushButton("Browse...").click();
			new DefaultShell("Select a Folder");
			new DefaultTreeItem(exportLocation).select();
			new PushButton("OK").click();
			new LabeledText("Name:").setText(exportName);
			new PushButton("OK").click();
			new DefaultShell("Export Teiid DDL");
		}else if(CUSTOM_WIZARD.equals(type)){
			throw new UnsupportedOperationException("not implemented yet");
		}
		finish();
	}
}
