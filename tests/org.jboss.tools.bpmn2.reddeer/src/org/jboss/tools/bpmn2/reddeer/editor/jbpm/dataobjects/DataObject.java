package org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Construct;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DataObjectTab;

/**
 * 
 */
public class DataObject extends Construct {

	/**
	 * 
	 * @param name
	 */
	public DataObject(String name) {
		super(name, ConstructType.DATA_OBJECT);
	}

	/**
	 * 
	 * @param name
	 */
	public void setDataType(String name) {
		properties.getTab("Data Object", DataObjectTab.class).setDataType(name);
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
