package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;
import org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.view.BPMN2PropertiesView;

/**
 *  Available output association types in BPMN2.
 *  
 *  FROM: DataInput
 *   - Name
 * 	 - Data State
 * 	 - Data Type
 *  TO:   Variable|Transformation
 *   - source
 *   
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class InputParameterMapping implements ParameterMapping {

	protected final String SECTION = "Input Parameter Mapping"; 
	
	protected BPMN2PropertiesView propertiesView = new BPMN2PropertiesView();
	protected SWTBot bot = new SWTBot();
	
	protected MappingSide from;
	protected MappingSide to;
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public InputParameterMapping(MappingSide from, MappingSide to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public void add() {
		bot.toolbarButtonWithTooltip("Add", propertiesView.indexOfSection(SECTION)).click();
		from.add();
		to.add();
		bot.toolbarButtonWithTooltip("Close").click();
	}

	@Override
	public void remove() {
		bot.table(0).select(from.getValue());
		bot.toolbarButtonWithTooltip("Remove", propertiesView.indexOfSection(SECTION)).click();
	}
	
}
