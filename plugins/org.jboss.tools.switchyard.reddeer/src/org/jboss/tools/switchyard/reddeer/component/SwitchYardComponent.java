package org.jboss.tools.switchyard.reddeer.component;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
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
		return showProperties(true);
	}
	
	public CompositePropertiesDialog showProperties(boolean isComponent) {
		getContextButton("Properties").click();
		if (isComponent) {
			return new CompositePropertiesDialog(tooltip).activate();
		} else {
			return new CompositePropertiesDialog("").activate();
		}	
	}

	public void delete() {
		getContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new DefaultShell(deleteShellText);
		new PushButton("Yes").click();
		new WaitWhile(new ShellIsAvailable(deleteShellText));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new SwitchYardEditor().save();
	}

}
