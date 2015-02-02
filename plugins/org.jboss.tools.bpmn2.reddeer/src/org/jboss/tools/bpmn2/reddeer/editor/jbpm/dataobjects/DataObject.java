package org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DataObjectTab;

/**
 * 
 */
public class DataObject extends Element {

	/**
	 * 
	 * @param name
	 */
	public DataObject(String name) {
		super(name, ElementType.DATA_OBJECT);
	}

	/**
	 * 
	 * @param name
	 */
	public void setDataType(String name) {
		properties.getTab("Data Object", DataObjectTab.class).setDataType(name);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ElementType)
	 */
	@Override
	public Element append(String name, ElementType constructType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ElementType, org.jboss.tools.bpmn2.reddeer.editor.Position)
	 */
	@Override
	public Element append(String name, ElementType constructType, Position position) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ElementType, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType)
	 */
	@Override
	public Element append(String name, ElementType constructType, ConnectionType connectionType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#append(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ElementType, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType, org.jboss.tools.bpmn2.reddeer.editor.Position)
	 */
	@Override
	public Element append(String name, ElementType constructType, ConnectionType connectionType, Position relativePosition) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#connectTo(org.jboss.tools.bpmn2.reddeer.editor.Element)
	 */
	@Override
	public void connectTo(Element construct) {
		this.connectTo(construct, ConnectionType.ASSOCIATION_UNDIRECTED);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Element#connectTo(org.jboss.tools.bpmn2.reddeer.editor.Element, org.jboss.tools.bpmn2.reddeer.editor.ConnectionType)
	 */
	@Override
	public void connectTo(Element construct, ConnectionType connectionType) {
		if (connectionType == ConnectionType.SEQUENCE_FLOW) {
			throw new UnsupportedOperationException();
		}
		super.connectTo(construct, connectionType);
	}
	
}
