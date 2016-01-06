package org.jboss.tools.fuse.reddeer.debug;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Class represents a Breakpoint in Breakpoints View
 * 
 * @author tsedmik
 */
public class Breakpoint extends DefaultTreeItem {

	private static Logger log = Logger.getLogger(Breakpoint.class);

	public Breakpoint(String label) {

		super(label);
	}

	/**
	 * Removes the breakpoint
	 */
	public void remove() {

		doOperation("Remove");
	}

	/**
	 * Disables the breakpoint
	 */
	public void disable() {

		doOperation("Disable");
	}

	/**
	 * Enables the breakpoint
	 */
	public void enable() {

		doOperation("Enable");
	}

	/**
	 * Checks whether is given operation enabled in the context menu of the breakpoint
	 * 
	 * @param operation
	 *            Name of operation in the context menu
	 * @return true - operation is available and enabled, false - otherwise
	 */
	public boolean isOperationEnabled(String operation) {

		log.debug("Checking operation '" + operation + "' on breakpoint:" + getText());
		select();
		try {
			ContextMenu menuitem = new ContextMenu(operation);
			return menuitem.isEnabled();
		} catch (CoreLayerException ex) {
			log.debug("Operation '" + operation + "' not found!");
			return false;
		}
	}

	/**
	 * Tries to perform given operation on the breakpoint. Operations are selected from the context menu of the
	 * breakpoint.
	 * 
	 * @param operation
	 *            Name of operation in the context menu
	 */
	public void doOperation(String operation) {

		log.debug("Performing '" + operation + "' on breakpoint: " + getText());
		select();
		ContextMenu menuitem = new ContextMenu(operation);
		if (menuitem.isEnabled()) {
			menuitem.select();
			log.debug("Operation '" + operation + "' was performed");
		} else {
			log.debug("Operation '" + operation + "' was NOT performed (item is not enabled)");
		}
	}

	/**
	 * Checks whether is the breakpoint enabled
	 */
	@Override
	public boolean isEnabled() {

		return isOperationEnabled("Disable");
	}
}
