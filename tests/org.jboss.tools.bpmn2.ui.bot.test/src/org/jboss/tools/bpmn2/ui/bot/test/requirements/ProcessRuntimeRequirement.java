package org.jboss.tools.bpmn2.ui.bot.test.requirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.log4j.Logger;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessRuntimeRequirement.ProcessRuntime;

/**
 * 
 */
public class ProcessRuntimeRequirement implements Requirement<ProcessRuntime> {

	private static final String RUNTIME_NAME = "jbpm6";

	private String runtimeDir = "default/test/runtime/dir";
	private String runtimeVer = "1.0.0.ER9";

	/**
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ProcessRuntime {

	}

	@Override
	public boolean canFulfill() {
		return true;//runtimeDir != null;
	}

	@Override
	public void fulfill() {
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		preferences.select("jBPM", "Installed jBPM Runtimes");

		boolean runtimeFound = false;
		Table table = new DefaultTable();
		if (table.rowCount() > 0) {
			for (int row = 0; row < table.rowCount(); row++) {
				if (table.getItem(row).getText(0).equals(RUNTIME_NAME)) {
					runtimeFound = true;
					break;
				}
			}
		}

		if (!runtimeFound) {
			new PushButton("Add...").click();
			new DefaultText(0).setText(RUNTIME_NAME);
			new DefaultText(1).setText(runtimeDir);
			new DefaultText(2).setText(runtimeVer);
			new FinishButton().click();
			try {
				preferences.wait((long) 5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			preferences.cancel();

			Logger.getLogger(ProcessRuntimeRequirement.class).info("jBPM Runtime '" + RUNTIME_NAME + "' added.");
		}

		table.getItem(RUNTIME_NAME).setChecked(true);
		new PushButton("OK").click();
	}

	@Override
	public void setDeclaration(ProcessRuntime declaration) {
		// Not required
	}

	/**
	 * 
	 * @param runtimeDir
	 */
	public void setRuntimeDir(String runtimeDir) {
		this.runtimeDir = runtimeDir;
	}

	@Override
	public void cleanUp() {
		// TODO cleanUp()
	}

}
