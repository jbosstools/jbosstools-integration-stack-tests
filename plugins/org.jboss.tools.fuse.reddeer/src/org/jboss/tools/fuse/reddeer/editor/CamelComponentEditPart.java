package org.jboss.tools.fuse.reddeer.editor;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.fuse.reddeer.component.AbstractURICamelComponent;

/**
 * EditPart inside the CamelEditor implementation which is looking for a given label inside the edit part.
 * 
 * @author tsedmik
 */
public class CamelComponentEditPart extends FuseEditPart {

	public CamelComponentEditPart(String label) {
		this(label, 0);
	}

	public CamelComponentEditPart(String label, int index) {
		super(new IsEditPartWithLabelPrefix(label), index);
	}

	public CamelComponentEditPart(AbstractURICamelComponent uriComponent) {
		super(new IsEditPartWithTooltip(uriComponent.getUri()), 0);
	}

	public Rectangle getBounds() {
		Rectangle bounds = getFigure().getBounds();
		final Rectangle rec = bounds.getCopy();
		getFigure().translateToAbsolute(rec);
		return rec;
	}

	public void remove() {
		select();
		new ContextMenu("Remove").select();
	}

	public void delete() {
		getContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new DefaultShell(deleteShellText);
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsAvailable(deleteShellText));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
