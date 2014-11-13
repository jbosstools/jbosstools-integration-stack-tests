package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.widget.Link;

/**
 * Wizard for creating a bean service.
 * 
 * @author apodhrad
 * 
 */
public class BeanServiceWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "New Bean Service";

	public BeanServiceWizard() {
	}

	public BeanServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public BeanServiceWizard setName(String name) {
		activate();
		new LabeledText("Name:").setText(name);
		return activate();
	}

	public BeanServiceWizard createNewInterface(String name) {
		activate();
		new Link("Interface:").click();
		new JavaInterfaceWizard().activate().setName(name).finish();
		return activate();
	}

	public BeanServiceWizard setExistingInterface(String name) {
		activate();
		new PushButton(2, "Browse...").click();
		new DefaultShell("");
		new DefaultText(0).setText("Service2");
		new WaitUntil(new TableHasRows(new DefaultTable(0)), TimePeriod.LONG);
		try {
		new DefaultTable(0).getItem("Service2 - com.example.switchyard.sy_transformer").select();
		} catch (Exception e) {
			System.out.println(new DefaultTable(0).getItem(0).getText());
			e.printStackTrace();
		}
		new PushButton("OK").click();
		return activate();
	}
}
