package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * FTP binding page
 * 
 * @author apodhrad
 * 
 */
public class FTPBindingPage extends OperationOptionsPage<FTPBindingPage> {

	public Text getHost() {
		return new LabeledText("Host");
	}

	public Text getPort() {
		return new LabeledText("Port (Default 21)");
	}

	public Text getUserName() {
		return new LabeledText("User Name");
	}

	public Text getPassword() {
		return new LabeledText("Password");
	}

	public CheckBox getUseBinaryTransferMode() {
		return new CheckBox("Use Binary Transfer Mode");
	}

	public Text getDirectory() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Directory*");
	}

	public Text getFileName() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "File Name");
	}

	public CheckBox getAutoCreateMissingDirectoriesinFilePath() {
		return new CheckBox(new DefaultGroup("File and Directory Options"),
				"Auto Create Missing Directories in File Path");
	}

	public Text getInclude() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Include");
	}

	public Text getExclude() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Exclude");
	}

	public CheckBox getDeleteFilesOnceProcessed() {
		return new CheckBox(new DefaultGroup("File and Directory Options"), "Delete Files Once Processed");
	}

	public CheckBox getProcessSubDirectoriesRecursively() {
		return new CheckBox(new DefaultGroup("File and Directory Options"), "Process Sub-Directories Recursively");
	}

	public Text getPreMove() {
		return new LabeledText(new DefaultGroup("Move Options"), "Pre-Move");
	}

	public Text getMove() {
		return new LabeledText(new DefaultGroup("Move Options"), "Move (Default .camel)");
	}

	public Text getMoveFailed() {
		return new LabeledText(new DefaultGroup("Move Options"), "Move Failed");
	}

	public Text getDelayBetweenPolls() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Delay Between Polls (MS) (Default 500)");
	}

	public Text getMaxMessagesPerPoll() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Max Messages Per Poll (Default 0)");
	}

}
