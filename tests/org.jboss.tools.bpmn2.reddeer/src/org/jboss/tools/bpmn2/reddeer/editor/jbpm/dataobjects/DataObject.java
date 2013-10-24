package org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Construct;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataObject extends Construct {

	public DataObject(String name) {
		super(name, ConstructType.DATA_OBJECT);
	}

	/**
	 * 
	 * @param name
	 */
	protected void setDataObjectName(String name) {
		properties.selectTab(type.toToolName());
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param dataType
	 */
	public void setDataObjectType(String dataType) {
		properties.selectTab(type.toToolName());
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Data Type");
		
		if (properties.contains(nameBox, dataType)) {
			nameBox.setSelection(dataType);
		} else {
			new DataType(dataType).add();
		}
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType)
	 */
	@Override
	public void append(String name, ConstructType constructType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType, org.jboss.tools.bpmn2.reddeer.editor.Position)
	 */
	@Override
	public void append(String name, ConstructType constructType, Position position) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType)
	 */
	@Override
	public void append(String name, ConstructType constructType, ConnectionType connectionType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType, org.jboss.tools.bpmn2.reddeer.editor.Position)
	 */
	@Override
	public void append(String name, ConstructType constructType, ConnectionType connectionType, Position relativePosition) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#connectTo(org.jboss.tools.bpmn2.reddeer.editor.Construct)
	 */
	@Override
	public void connectTo(Construct construct) {
		this.connectTo(construct, ConnectionType.ASSOCIATION_UNDIRECTED);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#connectTo(org.jboss.tools.bpmn2.reddeer.editor.Construct, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType)
	 */
	@Override
	public void connectTo(Construct construct, ConnectionType connectionType) {
		if (connectionType == ConnectionType.SEQUENCE_FLOW) {
			throw new UnsupportedOperationException();
		}
		super.connectTo(construct, connectionType);
	}
	
}
