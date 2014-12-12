package org.jboss.tools.bpmn2.reddeer.editor.graphiti;

import org.eclipse.gef.EditPart;
import org.jboss.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AbstractSetUpCTab;

/**
 * Class for setting element properties
 * @author jomarko
 *
 */
public class PropertiesGraphitiEditPart extends AbstractGraphitiEditPart {

	public PropertiesGraphitiEditPart(EditPart editPart) {
		super(editPart);
	}
	
		public void setUpTabs(AbstractSetUpCTab... tabs) {
		
			getContextButton("Show Properties").click();
			new DefaultShell().setFocus();
			for(AbstractSetUpCTab tab : tabs) {
				
				if(tab != null) {
					try{
						new DefaultTabItem(tab.getTabLabel()).activate();
					}catch(SWTLayerException e) {
						//General tab is showed only if has element more tabs in properties view
					}
					tab.setUpCTab();
				}
			}
			
			new DefaultShell().setFocus();
			new PushButton("OK").click();
		
		}
}
