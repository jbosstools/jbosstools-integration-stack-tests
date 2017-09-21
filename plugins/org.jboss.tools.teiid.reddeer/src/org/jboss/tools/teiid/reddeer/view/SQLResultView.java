package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

public class SQLResultView extends WorkbenchView {

	public SQLResultView() {
		super("Data Management", "SQL Results");
	}

	public SQLResult getByOperation(String operation) {
		String operationWithoutSpaces = operation.replaceAll("\\s", "");
		open();
		AbstractWait.sleep(TimePeriod.getCustom(1));

		for (TreeItem item : new DefaultTree().getItems()) {
			if (item.getCell(1).replaceAll("\\s", "").equals(operationWithoutSpaces)) {
				return new SQLResult(item);
			}
		}

		throw new CoreLayerException("Cannot find sql result for operation " + operation);
	}

	public void enableUnresolvableCps() {
		open();
		activate();
		new DefaultToolItem("Filter Results").click();
		new DefaultShell("SQL Results Filters");
		new CheckBox("Display results of unresolvable connection profiles").toggle(true);
		new PushButton("OK").click();

	}
	
	public void clear(){
		open();
		DefaultToolItem button = new DefaultToolItem(2);
		if(button.isEnabled()){
			button.click();
		}
	}
}
