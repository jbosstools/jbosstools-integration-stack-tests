package org.jboss.tools.switchyard.reddeer.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 * 
 */
public class ContextButtonEntry {

	protected IContextButtonEntry contextButtonEntry;

	public ContextButtonEntry(IContextButtonEntry contextButtonEntry) {
		this.contextButtonEntry = contextButtonEntry;
	}

	public void click() {
		Display.asyncExec(new Runnable() {
			@Override
			public void run() {
				contextButtonEntry.execute();
			}
		});
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	public String getText() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return contextButtonEntry.getText();
			}
		});
	}

	public List<ContextButtonEntry> getContextButtonEntries() {
		List<ContextButtonEntry> entries = new ArrayList<ContextButtonEntry>();
		if (contextButtonEntry instanceof org.eclipse.graphiti.tb.ContextButtonEntry) {
			org.eclipse.graphiti.tb.ContextButtonEntry entry = (org.eclipse.graphiti.tb.ContextButtonEntry) contextButtonEntry;
			List<IContextButtonEntry> menuEntries = entry.getContextButtonMenuEntries();
			for (IContextButtonEntry menuEntry : menuEntries) {
				entries.add(new ContextButtonEntry(menuEntry));
			}
		}
		return entries;
	}
}
