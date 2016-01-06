package org.jboss.tools.drools.reddeer.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class AuditView extends WorkbenchView {
	private static final Logger LOGGER = Logger.getLogger(AuditView.class);

	public AuditView() {
		super("Drools", "Audit");
	}

	public void openLog(final String logLocation) {
		// Using the toolbar, the file dialog is opened
		// new DefaultToolItem("Open Log").click();

		// Retrieves the AuditView object from all opened objects
		final IViewPart part = Display.syncExec(new ResultRunnable<IViewPart>() {
			public IViewPart run() {
				for (IViewReference ref : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getViewReferences()) {
					if (ref.getId().contains("AuditView")) {
						return ref.getView(false);
					}
				}
				return null;
			}
		});

		// call the setLogFile method using reflection (to avoid Drools plugin dependency)
		if (part != null) {
			Display.syncExec(new Runnable() {
				public void run() {
					try {
						Method m = part.getClass().getMethod("setLogFile", String.class);
						m.invoke(part, logLocation);
					} catch (NoSuchMethodException ex) {
						LOGGER.error(ex);
					} catch (InvocationTargetException ex) {
						LOGGER.error(ex);
					} catch (IllegalAccessException ex) {
						LOGGER.error(ex);
					}
				}
			});
		}
	}

	public void refreshLog() {
		new DefaultToolItem("Refresh Log").click();
	}

	public void clearLog() {
		new DefaultToolItem("Clear Log").click();
	}

	public List<String> getEvents() {
		List<String> result = new LinkedList<String>();

		for (TreeItem item : new DefaultTree().getItems()) {
			result.add(item.getText());
		}

		return result;
	}
}
