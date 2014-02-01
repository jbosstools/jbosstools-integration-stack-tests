package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Process extends org.jboss.tools.bpmn2.reddeer.editor.Process {

	/**
	 * 
	 * @param name
	 */
	public Process(String name) {
		super(name);
		select();
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		properties.selectTab("Process");
		new LabeledText("Id").setText(id);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setProcessName(String name) {
		properties.selectTab("Process");
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		properties.selectTab("Version");
		new LabeledText("Id").setText(version);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setPackageName(String name) {
		properties.selectTab("Process");
		new LabeledText("Package Name").setText(name);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setAddHoc(boolean b) {
		properties.selectTab("Process");
		properties.selectCheckBox(new CheckBox(0), b); // Ad Hoc
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setExecutable(boolean b) {
		properties.selectTab("Process");
		properties.selectCheckBox(new CheckBox(1), b); // Is Executable
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		properties.selectTab("Definitions");
		properties.toolbarButton("Data Type List", "Add").click();
		bot.textWithLabel("Structure").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addImport(String dataType) {
		properties.selectTab("Definitions");
		properties.toolbarButton("Imports", "Add").click();
		
		String dataTypeLabel = dataType.substring(dataType.lastIndexOf(".") + 1) + " - " + dataType;
		/*
		 * Must by typed for the listener to get activated.
		 */
		SWTBot windowBot = bot.shell("Browse for a Java type to Import").bot();
		SWTBotText text = windowBot.textWithLabel("Type:");
		text.setText(dataType);
		text.typeText(" ");
		windowBot.tree().select(dataTypeLabel);
		
		new PushButton("OK").click();
	}
	
	public void addMessage(String name, String dataType) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 2).click();
		bot.textWithLabel("Name").setText(name);
		
		new DataType(dataType).add();

		maximizeAndRestorePropertiesView();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addError(String name, String code, DataType dataType) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 3).click();
		if (name != null && !name.isEmpty())
			bot.textWithLabel("Name").setText(name);
		if (code != null && !code.isEmpty())
			bot.textWithLabel("Error Code").setText(code);
		
		if (dataType != null) {
			dataType.add();
		}
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addEscalation(String name, String code) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 4).click();
		bot.textWithLabel("Escalation Code").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addSignal(String name) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 5).click();
		bot.textWithLabel("Name").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addGlobalVariable(String name, String dataType) {
		properties.selectTab("Data Items");
		
		bot.toolbarButtonWithTooltip("Add", 0).click();
		bot.textWithLabel("Name").setText(name);
		
		new DataType(dataType).add();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addLocalVariable(String name, String dataType) {
		properties.selectTab("Data Items");
		
//		// TBD: the name is not the process name but file name
//		properties.toolbarButton("Variable List for Process \"" + name + "\"", "Add").click();
		
		bot.toolbarButtonWithTooltip("Add", 1).click();
		bot.textWithLabel("Name").setText(name);

		new DataType(dataType).add();
		
		maximizeAndRestorePropertiesView();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addInterface() {
		throw new UnsupportedOperationException();
	}

	/*
	 * Bug in BPMN2 Editor where the close button can be found only after it was made visible. Maximize
	 * and then restore the window to display the button.
	 */
	private void maximizeAndRestorePropertiesView() {
		for (int i=0; i<2; i++) {
			properties.maximize();
		}
	}
}
