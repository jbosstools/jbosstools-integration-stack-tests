package org.jboss.tools.bpmn2.reddeer.editor.graphiti;

import org.eclipse.gef.EditPart;
import org.jboss.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.tools.bpmn2.reddeer.properties.setup.SetUpAble;

/**
 * Class for setting element properties
 * 
 * @author jomarko
 *
 */
public class PropertiesGraphitiEditPart extends AbstractGraphitiEditPart {

	public PropertiesGraphitiEditPart(EditPart editPart) {
		super(editPart);
	}

	public void setUpTabs(SetUpAble... tabs) {

		getContextButton("Show Properties").click();
		Shell shell = new DefaultShell();
		String shellLabel = shell.getText();
		shell.setFocus();
		try {
			for (SetUpAble tab : tabs) {

				if (tab != null) {
					try {
						new DefaultTabItem(tab.getTabLabel()).activate();
					} catch (SWTLayerException e) {
						// General tab is showed only if has element more tabs in properties view
					}
					tab.setUpCTab();
				}
			}
		} finally {
			new DefaultShell(shellLabel);
			new PushButton("OK").click();
		}
	}
}
