package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddActorSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddLocalVariableSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.LabeledTextSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.RemoveActorSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class UserTask extends Task {

	private static final String USER_TASK = "User Task";

	/**
	 * 
	 * @param name
	 */
	public UserTask(String name) {
		super(name, ElementType.USER_TASK);
	}
	
	public UserTask(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setTaskName(String name) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Task Name", name));
	}
	
	public String getTaskName() {
		propertiesHandler.selectTabInPropertiesView(USER_TASK);
		return new LabeledText("Task Name").getText();
		
	}
	
	/**
	 * 
	 * @param priority
	 */
	public void setPriority(int priority) {
		setPriority(String.valueOf(priority));
	}
	
	/**
	 * USE ONLY FOR numbers bigger than int
	 * @param priority
	 */
	public void setPriority(String priority) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Priority", priority));
	}

	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Comment", comment));
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setGroupId(String id) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Group Id", id));
		
	}
	
	public String getGroupId() {
		propertiesHandler.selectTabInPropertiesView(USER_TASK);
		return new LabeledText("Group Id").getText();
	}
	
	/**
	 * 
	 * @param skippable
	 */
	public void setSkippable(boolean skippable) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(USER_TASK, "Skippable", skippable));
		
	}
	
	public boolean getSkippable() {
		propertiesHandler.activateGraphitiPropertiesView();
		Shell shell = new DefaultShell();
		shell.setFocus();
		new DefaultTabItem(USER_TASK).activate();
		boolean result =  new DefaultCheckBox("Skippable").isChecked();
		new PushButton("OK").click();
		return result;
	}
	
	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Content", content));
	}
	
	/**
	 * 
	 * @param locale
	 */
	public void setLocale(String locale) {
		propertiesHandler.setUp(new LabeledTextSetUpCTab(USER_TASK, "Locale", locale));
	}
	
	/**
	 * 
	 * @param actor
	 */
	public void addActor(String actor) {
		propertiesHandler.setUp(new AddActorSetUp(USER_TASK, actor));
	}
	
	public void removeActor(String actorName) {
		propertiesHandler.setUp(new RemoveActorSetUp(USER_TASK, actorName));
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(USER_TASK, "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(USER_TASK, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExitScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(USER_TASK, "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
	public void addLocalVariable(String varName, String dataType) {
		propertiesHandler.setUp(new AddLocalVariableSetUp(USER_TASK, varName, dataType));
	}

}
