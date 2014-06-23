package org.jboss.tools.reddeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.tools.reddeer.finder.PaletteEntryFinder;

/**
 * 
 */
public class DefaultPalette {

	private PaletteViewer paletteViewer;

	/**
	 * 
	 * @param paletteViewer
	 */
	public DefaultPalette(PaletteViewer paletteViewer) {
		if(paletteViewer == null) {
			throw new NullPointerException("PaletteViewer cannot be null!");
		}
		this.paletteViewer = paletteViewer;
	}

	/**
	 * 
	 * @param tool
	 */
	public void activateTool(String ... tool) {
		List<String> pathToEntry = Arrays.asList(tool);
		List<PaletteEntry> entries = getPaletteEntries(new PaletteEntryWithPath(pathToEntry));
		if (entries.isEmpty()) {
			throw new RuntimeException("Cannot find palette entry with path '" + pathToEntry + "'");
		}
		final ToolEntry toolEntry = (ToolEntry) entries.get(0);
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				paletteViewer.setActiveTool(toolEntry);
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public String getActiveTool() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return paletteViewer.getActiveTool().getLabel();
			}
		});
	}

	/**
	 * 
	 * @param matcher
	 * @return
	 */
	public List<PaletteEntry> getPaletteEntries(final Matcher<PaletteEntry> matcher) {
		return Display.syncExec(new ResultRunnable<List<PaletteEntry>>() {
			@Override
			public List<PaletteEntry> run() {
				PaletteRoot paletteRoot = paletteViewer.getPaletteRoot();
				return new PaletteEntryFinder().find(paletteRoot, matcher);
			}

		});
	}

	/**
	 * 
	 */
	public class PaletteEntryWithPath extends BaseMatcher<PaletteEntry> {

		private String tool;

		private List<String> section;

		/**
		 * 
		 * @param pathToEntry
		 */
		public PaletteEntryWithPath(List<String> pathToEntry) {
			this.tool = pathToEntry.get(pathToEntry.size() - 1);
			this.section = pathToEntry.subList(0, pathToEntry.size() - 1);
			
		}

		@Override
		public boolean matches(Object obj) {
			List<String> entrySection = new ArrayList<String>();
			
			if (obj instanceof ToolEntry) {
				ToolEntry entry = (ToolEntry) obj;
				if (entry.getLabel().equals(tool)) {
					PaletteEntry parent = entry.getParent();
					do {
						String label = parent.getLabel();
						if (label != null) {
							entrySection.add(label);
						}
					} while ((parent = parent.getParent()) != null);
				}
				Collections.reverse(entrySection);
				return section.equals(entrySection);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {
		}

	}

}

