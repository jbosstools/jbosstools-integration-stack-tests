package org.jboss.tools.drools.reddeer.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.drools.reddeer.util.ItemLookup;

public class WorkingMemoryView extends WorkbenchView {
	private static final Pattern OBJECT_PATTERN = Pattern.compile("\\[\\d+\\]= ([^ ]+) +\\(id=\\d+\\)");
	private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(\\w+)= ([^\\(]+).*");
	private static final Logger LOGGER = Logger.getLogger(WorkingMemoryView.class);

	public WorkingMemoryView() {
		super("Drools", "Working Memory");
	}

	public List<String> getObjects() {
		open();
		List<String> result = new LinkedList<String>();

		Matcher m;
		String text;
		for (TreeItem item : new DefaultTree().getItems()) {
			text = item.getText();
			m = OBJECT_PATTERN.matcher(text);
			if (m.matches()) {
				result.add(m.group(1));
			} else {
				LOGGER.error(String.format("Non matching string: '%s'", text));
			}
		}

		return result;
	}

	public Map<String, String> getObjectAttributes(String objectName) {
		open();
		Map<String, String> result = new HashMap<String, String>();

		TreeItem obj = ItemLookup.getItemInTree(new DefaultTree(),
				new RegexMatcher(".*" + Pattern.quote(objectName) + ".*"));
		if (obj == null) {
			throw new IllegalArgumentException(String.format("Unable to find '%s'", objectName));
		}
		Matcher m;
		String text;
		for (TreeItem item : obj.getItems()) {
			text = item.getText();
			m = ATTRIBUTE_PATTERN.matcher(text);
			if (m.matches()) {
				result.put(m.group(1), m.group(2).trim());
			} else {
				LOGGER.error(String.format("Non matching string: '%s'", text));
			}
		}

		return result;
	}
}
