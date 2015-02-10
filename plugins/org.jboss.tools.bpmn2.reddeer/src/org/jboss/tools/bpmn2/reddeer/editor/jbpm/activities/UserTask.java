package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.UserTaskTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.LabeledTextSetUpCTab;

/**
 * 
 */
public class UserTask extends Task {

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
		//properties.getTab("User Task", UserTaskTab.class).setTaskName(name);
		graphitiProperties.setUpTabs(new LabeledTextSetUpCTab("User Task", "Task Name", name));
	}
	
	/**
	 * 
	 * @param priority
	 */
	public void setPriority(int priority) {
		properties.getTab("User Task", UserTaskTab.class).setPriority(String.valueOf(priority));
	}

	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		properties.getTab("User Task", UserTaskTab.class).setComment(comment);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setGroupId(String id) {
		properties.getTab("User Task", UserTaskTab.class).setGroupId(id);
	}
	
	/**
	 * 
	 * @param skippable
	 */
	public void setSkippable(boolean skippable) {
		//properties.getTab("User Task", UserTaskTab.class).setSkippable(skippable);
		graphitiProperties.setUpTabs(new CheckBoxSetUpCTab("User Task", "Skippable", skippable));
		
	}
	
	public boolean getSkippable() {
		graphitiProperties.getContextButton("Show Properties").click();
		Shell shell = new DefaultShell();
		shell.setFocus();
		new DefaultTabItem("User Task").activate();
		boolean result =  new DefaultCheckBox("Skippable").isChecked();
		new PushButton("OK").click();
		return result;
	}
	
	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		properties.getTab("User Task", UserTaskTab.class).setContent(content);
	}
	
	/**
	 * 
	 * @param locale
	 */
	public void setLocale(String locale) {
		//properties.getTab("User Task", UserTaskTab.class).setLocale(locale);
		graphitiProperties.setUpTabs(new LabeledTextSetUpCTab("User Task", "Locale", locale));
	}
	
	/**
	 * 
	 * @param actor
	 */
	public void addActor(String actor) {
		properties.getTab("User Task", UserTaskTab.class).addActor(actor);
	}
	
	public void removeActor(String name) {
		properties.getTab("User Task", UserTaskTab.class).removeActor(name);
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("User Task", UserTaskTab.class).setIsForCompensation(value);
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab("User Task", UserTaskTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExitScript(String language, String script) {
		properties.getTab("User Task", UserTaskTab.class).setOnExitScript(new Expression(language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
//		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
		graphitiProperties.setUpTabs(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
	public void addLocalVariable(String varName, String dataType) {
		properties.getTab("User Task", UserTaskTab.class).addLocalVariable(varName, dataType);
	}

}
