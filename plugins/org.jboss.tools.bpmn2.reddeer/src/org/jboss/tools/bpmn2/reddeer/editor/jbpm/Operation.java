package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import java.util.List;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.AbstractTable;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ErrorDialog;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.MessageDialog;

/**
 * 
 */
public class Operation {

	private String name;
	private Message inMessage;
	private Message outMessage;
	private ErrorRef errorRef;

	/**
	 * 
	 * @param name
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public Operation(String name, Message inMessage, Message outMessage, ErrorRef errorRef) {
		this.name = name;
		this.inMessage = inMessage;
		this.outMessage = outMessage;
		this.errorRef = errorRef;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public Message getInMessage() {
		return inMessage;
	}

	/**
	 * 
	 * @return
	 */
	public Message getOutMessage() {
		return outMessage;
	}

	/**
	 * 
	 * @return
	 */
	public ErrorRef getErrorRef() {
		return errorRef;
	}

	/**
	 * Perform user actions which are required to set up this object in the UI.
	 */
	public void setUp() {
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(name);

		new DefaultTabItem("Operation").activate();

		if (inMessage != null) {
			String inMsgComboItem = inMessage.getName() + "(" + inMessage.getDataType() + ")";
			Combo inMsgCombo = new LabeledCombo("In Message");

			if (!inMsgCombo.getItems().contains(inMsgComboItem)) {
				new PushButton(1).click();
				new MessageDialog().add(inMessage);
			}

			inMsgCombo.setSelection(inMsgComboItem);
		} else {
			throw new IllegalStateException("Operation: " + name + " must have specified in message");
		}

		if (outMessage != null) {
			String outMsgComboItem = outMessage.getName() + "(" + outMessage.getDataType() + ")";
			Combo outMsgCombo = new LabeledCombo("Out Message");

			if (!outMsgCombo.getItems().contains(outMsgComboItem)) {
				new PushButton(3).click();
				new MessageDialog().add(outMessage);
			}

			outMsgCombo.setSelection(outMsgComboItem);
		} else {
			throw new IllegalStateException("Operation: " + name + " must have specified out message");
		}

		if (errorRef != null) {
			new PushButton(5).click();
			new DefaultShell("Select elements -- " + name);
			String errorItem = errorRef.getName() + "(" + errorRef.getDataType() + ")";
			AbstractTable errorTable = new DefaultTable(0);
			List<TableItem> errors = errorTable.getItems();

			for (TableItem error : errors) {
				if (error.getText().equals(errorItem)) {
					errorTable.select(errorItem);
					new PushButton("Add").click();
					new PushButton("OK").click();
					new WaitWhile(new ShellWithTextIsActive("Select elements -- " + name), TimePeriod.LONG);
					new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
					break;
				}

				if (error.equals(errors.get(errors.size() - 1))) {
					new PushButton(5).click(); // close selecting errors
					new PushButton(5).click(); // open add error dialog
					new ErrorDialog().add(errorRef);
				}
			}
		}
	}

}
