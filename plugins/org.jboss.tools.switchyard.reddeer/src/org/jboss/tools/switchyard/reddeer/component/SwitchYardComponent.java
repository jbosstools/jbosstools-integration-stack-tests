package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.matcher.WithTooltip;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesDialog;

public class SwitchYardComponent extends AbstractGraphitiEditPart {

	protected String tooltip;

	public SwitchYardComponent(String tooltip) {
		this(tooltip, 0);
	}

	public SwitchYardComponent(String tooltip, int index) {
		super(new WithTooltip(tooltip), activateSwitchYardEditor(index));
		this.tooltip = tooltip;
	}

	/**
	 * Activates SwitchYard editor before calling super constructor.
	 * 
	 * @param index
	 *            index
	 * @return the same index
	 */
	private static int activateSwitchYardEditor(int index) {
		new DefaultEditor(SwitchYardEditor.TITLE);
		new DefaultCTabItem("Design").activate();
		return index;
	}

	public String getTooltip() {
		return tooltip;
	}

	public CompositePropertiesDialog showProperties() {
		getContextButton("Properties").click();
		return new CompositePropertiesDialog(tooltip).activate();
	}

	public void delete() {
		getContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new DefaultShell(deleteShellText);
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsAvailable(deleteShellText));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new SwitchYardEditor().save();
	}

}
