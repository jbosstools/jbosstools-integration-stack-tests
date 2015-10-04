package org.jboss.tools.teiid.reddeer.preference;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.spinner.DefaultSpinner;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidDesignerPreferencePage extends PreferencePage {

	public TeiidDesignerPreferencePage() {
		super("Teiid Designer");
		
	}

	
	public void setDefaultTeiidInstanceTargetedVersion(String version){
		open(); 
		new DefaultCombo().setSelection(version);
		close();
	}
	
	//TODO deploy preference for teiid import - seconds to wait
	public void setTeiidConnectionImporterTimeout(int secs){
		open(); 
		new DefaultSpinner(new WithTooltipTextMatcher("Set the timeout (in sec) for the Teiid Connection Importer. (use 0 for no timeout)")).setValue(secs);
		close();
	}
	
	
	public void setAutoToggleDataRoleChildren(boolean check){
		open();
		new CheckBox(new DefaultGroup("Preview Data/VDB Execution"),"Enable auto-toggling children of the checked model object").toggle(check);
		close();

	}
	
	
	public void close(){
		new PushButton("Apply").click();
		if (new SWTWorkbenchBot().activeShell().getText().equals("Changing Teiid Instance version")){
			new PushButton("Yes").click();
		}
		new PushButton("OK").click();
	}
	
	public void open() {
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		preferences.select(this);
	}
	
}
