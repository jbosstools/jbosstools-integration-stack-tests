package org.jboss.tools.bpmn2.reddeer.editor.properties;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.toolbar.AbstractToolItem;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.util.Display;

public class SectionToolItem extends AbstractToolItem {
	public SectionToolItem(final String sectionLabel, final String itemLabel) {
		int index = 0;
		boolean catchedException = false;
		do{
			try{
				final Widget w = WidgetLookup.getInstance().activeWidget(null, org.eclipse.swt.custom.SashForm.class, index);
				
				Display.syncExec(new Runnable() {
					
					@Override
					public void run() {
						for(Control c : ((org.eclipse.swt.custom.SashForm)w).getChildren()) {
							if(c != null && c instanceof org.eclipse.ui.forms.widgets.Section) {
								Section section = (Section) c;
								if(section.getText().compareTo(sectionLabel) == 0) {
									for(Control sectionChildren : section.getChildren()) {
										if(sectionChildren instanceof org.eclipse.swt.widgets.ToolBar) {
											for(ToolItem toolBarItem : ((org.eclipse.swt.widgets.ToolBar) sectionChildren).getItems()) {
												if(toolBarItem.getToolTipText().compareTo(itemLabel) == 0) {
													toolItem = toolBarItem;
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				});
				index++;
			} catch(Exception e) {
				catchedException = true;
			}
		}while(toolItem == null && !catchedException);
		
		if(toolItem == null) {
			throw new SWTLayerException("Unable to find ToolItem with label: " + itemLabel + " in section: " + sectionLabel);
		}
	}
	
	
}
