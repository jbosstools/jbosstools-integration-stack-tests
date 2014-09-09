package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.matcher.WithTooltip;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;

public class SwitchYardComponent extends AbstractGraphitiEditPart {

	protected String tooltip;

	public SwitchYardComponent(String tooltip) {
		this(tooltip, 0);
	}

	public SwitchYardComponent(String tooltip, int index) {
		super(new WithTooltip(tooltip), index);
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return tooltip;
	}

	public CompositePropertiesPage showProperties() {
		getContextButton("Properties").click();
		return new CompositePropertiesPage(tooltip).activate();
	}

	public void delete() {
		getContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new DefaultShell(deleteShellText);
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsAvailable(deleteShellText));
		new WaitWhile(new JobIsRunning());
	}

}
