package org.jboss.tools.bpmn2.reddeer.editor.properties;

import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;

public class SectionToolItem extends DefaultToolItem {

	// TODO Test whether the implementation is correct
	public SectionToolItem(final String sectionLabel, final String itemLabel) {
		super(new DefaultSection(sectionLabel), itemLabel);
	}

}
