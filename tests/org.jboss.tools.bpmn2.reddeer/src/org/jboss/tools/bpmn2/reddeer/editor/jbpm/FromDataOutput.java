package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Represents the target side of parameter mapping.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class FromDataOutput implements MappingSide {

	private String name;
	
	private String dataType;

	/**
	 * Creates a new instance of FromDataOutput.
	 * @param name
	 */
	public FromDataOutput(String name) {
		this(name, null);
	}
	
	/**
	 * Creates a new instance of FromDataOutput.
	 * 
	 * @param name
	 * @param dataType
	 */
	public FromDataOutput(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	
	/**
	 * Define new data output.
	 */
	public void add() {
		new LabeledText("Name").setText(name);
		
		if (dataType != null) {
			try {
				new LabeledCombo("Data Type").setSelection(dataType);
			} catch (Exception e) {
				new PushButton(0).click();
				SWTBot windowBot = new SWTBot().shell("Create New Data Type").bot();
				windowBot.textWithLabel("Structure").setText(dataType);
				windowBot.button("OK").click();
			}
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public String getValue() {
		return name;
	}
	
}
