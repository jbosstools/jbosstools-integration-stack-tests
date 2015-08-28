package org.jboss.tools.bpmn2.reddeer;

import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.core.util.Display;

/**
 * 
 */
public class DefaultSection extends AbstractSWTBot<Section> implements ReferencedComposite{

	private SWTBot bot;

	/**
	 * 
	 * @param text
	 */
	public DefaultSection(String text) {
		this(getSection(text));
	}
	
	/**
	 * 
	 * @param section
	 */
	private DefaultSection(Section section) {
		super(section);
		bot = new SWTBot(section);
	}
	
	/**
	 * 
	 * @return
	 */
	public SWTBotTable getTable() {
		return bot.table();
	}
	
	/**
	 * 
	 * @param tooltip
	 * @return
	 */
	public SWTBotToolbarButton getToolbarButton(String tooltip) {
		return bot.toolbarButtonWithTooltip(tooltip);
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public SWTBotText getText(String label) {
		return bot.textWithLabel(label);
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public SWTBotCombo getComboBox(String label) {
		return bot.comboBoxWithLabel(label);
	}
	
	/**
	 * 
	 * @param expanded
	 */
	public void setExpanded(final boolean expanded) {
		Display.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				widget.setExpanded(expanded);
				
			}
		});
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private static Section getSection(final String name) {
		return UIThreadRunnable.syncExec(new Result<Section>() {

			public Section run() {
				List<? extends Section> sectionList = new SWTBot().widgets(WidgetMatcherFactory.widgetOfType(Section.class));
				for (Section s : sectionList) {
					if (name.equals(s.getText())) {
						return s;
					}
				}
				return null;
			}
			
		});
		
	}

	@Override
	public Control getControl() {
		return widget;
	}
}
