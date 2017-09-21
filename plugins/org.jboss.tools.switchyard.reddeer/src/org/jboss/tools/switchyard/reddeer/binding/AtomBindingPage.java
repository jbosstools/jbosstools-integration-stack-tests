package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Atom binding page
 * 
 * @author apodhrad
 * 
 */
public class AtomBindingPage extends OperationOptionsPage<AtomBindingPage> {

	public LabeledText getDelayBetweenPolls() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Delay Between Polls (MS) (Default 500)");
	}

	public LabeledText getLastUpdateStartingTimestamp() {
		return new LabeledText(new DefaultGroup("Split Entry Options"), "Last Update (Starting Timestamp)");
	}

	public LabeledText getFeedURI() {
		return new LabeledText("Feed URI*");
	}

	public CheckBox getSortEntriesbyDate() {
		return new CheckBox(new DefaultGroup("Split Entry Options"), "Sort Entries (by Date)");
	}

	public CheckBox getFilter() {
		return new CheckBox(new DefaultGroup("Split Entry Options"), "Filter");
	}

	public CheckBox getSplitEntries() {
		return new CheckBox(new DefaultGroup("Split Entry Options"), "Split Entries");
	}

}
