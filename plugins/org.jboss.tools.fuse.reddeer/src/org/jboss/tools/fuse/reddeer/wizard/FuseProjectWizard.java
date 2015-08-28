package org.jboss.tools.fuse.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 */
public class FuseProjectWizard extends NewWizardDialog {

	public FuseProjectWizard() {
		super("Fuse Tooling", "Fuse Project");
	}

	public void setFilter(String text) {
		new LabeledText("Filter:").setText(text);
	}

	public void selectFirstArchetype() {
		new DefaultTable().select(0);
	}

	public void setProjectName(String name) {
		new DefaultCombo(0).setText(name);
	}

	public List<String> getArchetypes() {

		List<String> result = new ArrayList<String>();
		for (TableItem item : new DefaultTable().getItems()) {
			result.add(item.getText(1));
		}
		return result;
	}

	@Override
	public void finish() {

		log.info("Finish wizard");

		String shellText = new DefaultShell().getText();
		Button button = new PushButton("Finish");
		new WaitUntil(new WidgetIsEnabled(button));
		button.click();

		TimePeriod timeout = TimePeriod.getCustom(20 * 60 * 1000);
		new WaitWhile(new ShellWithTextIsActive(shellText), timeout);
		new WaitWhile(new JobIsRunning(), timeout);
	}
}
