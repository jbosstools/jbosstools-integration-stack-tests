package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SFTP binding page
 * 
 * @author apodhrad
 * 
 */
public class SFTPBindingPage extends OperationOptionsPage<SFTPBindingPage> {

	public LabeledText getPrivateKeyFilePassphrase() {
		return new LabeledText("Private Key File Passphrase");
	}

	public LabeledText getPrivateKeyFile() {
		return new LabeledText("Private Key File");
	}

	public LabeledText getMaxMessagesPerPoll() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Max Messages Per Poll (Default 0)");
	}

	public LabeledText getDelayBetweenPolls() {
		return new LabeledText(new DefaultGroup("Poll Options"), "Delay Between Polls (MS) (Default 500)");
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
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Exclude");
	}

	public LabeledText getInclude() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Include");
	}

	public LabeledText getFileName() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "File Name");
	}

	public LabeledText getDirectory() {
		return new LabeledText(new DefaultGroup("File and Directory Options"), "Directory");
	}

	public LabeledText getPassword() {
		return new LabeledText("Password");
	}

	public LabeledText getUserName() {
		return new LabeledText("User Name");
	}

	public LabeledText getPort() {
		return new LabeledText("Port (Default 21)");
	}

	public LabeledText getHost() {
		return new LabeledText("Host*");
	}

	public CheckBox getProcessSubDirectoriesRecursively() {
		return new CheckBox(new DefaultGroup("File and Directory Options"), "Process Sub-Directories Recursively");
	}

	public CheckBox getDeleteFilesOnceProcessed() {
		return new CheckBox(new DefaultGroup("File and Directory Options"), "Delete Files Once Processed");
	}

	public CheckBox getAutoCreateMissingDirectoriesinFilePath() {
		return new CheckBox(new DefaultGroup("File and Directory Options"),
				"Auto Create Missing Directories in File Path");
	}

	public CheckBox getUseBinaryTransferMode() {
		return new CheckBox("Use Binary Transfer Mode");
	}

}
