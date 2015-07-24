package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * File binding page
 * 
 * @author apodhrad
 * 
 */
public class FileBindingPage extends OperationOptionsPage<FileBindingPage> {

	public LabeledText getDelayBetweenPolls() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Delay Between Polls (MS) (Default 500)");
	}

	public LabeledText getMaxMessagesPerPoll() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Max Messages Per Poll (Default 0)");
	}

	public LabeledText getMoveFailed() {
		return new LabeledText(new DefaultGroup("Move Options"), "Move Failed");
	}

	public LabeledText getMove() {
		return new LabeledText(new DefaultGroup("Move Options"), "Move (Default .camel)");
	}

	public LabeledText getPreMove() {
		return new LabeledText(new DefaultGroup("Move Options"), "Pre-Move");
	}

	public LabeledText getExclude() {
		return new LabeledText("Exclude");
	}

	public LabeledText getInclude() {
		return new LabeledText("Include");
	}

	public LabeledText getFileName() {
		return new LabeledText("File Name");
	}

	public LabeledText getDirectory() {
		return new LabeledText("Directory*");
	}

	public CheckBox getAutoCreateMissingDirectoriesinFilePath() {
		return new CheckBox("Auto Create Missing Directories in File Path");
	}

	public Text getTempPrefix() {
		return new LabeledText("Temp Prefix");
	}

	public Text getFileExist() {
		return new LabeledText("File Exist");
	}

}
