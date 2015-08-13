package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class SQLResultView extends WorkbenchView {

	public SQLResultView() {
		super("Data Management", "SQL Results");
	}

	public SQLResult getByOperation(String operation) {

		open();
		AbstractWait.sleep(TimePeriod.getCustom(1));
		
		for (TreeItem item : new DefaultTree().getItems()) {
			if (item.getCell(1).trim().equals(operation)) {
				return new SQLResult(item);
			}
		}
		
		throw new WidgetNotFoundException("Cannot find sql result for operation " + operation);
	}
	
	
	public void enableUnresolvableCps(){
		open();
		new ViewToolItem("Filter Results").click();
		new DefaultShell("SQL Results Filters");
		new CheckBox("Display results of unresolvable connection profiles").toggle(true);
		new PushButton("OK").click();

	}
}
