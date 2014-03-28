package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * 
 */
public abstract class AbstractTask extends AbstractConstruct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractTask(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.Construct#addEvent(java.lang.String, org.jboss.tools.bpmn2.reddeer.editor.ConstructType)
	 */
	@Override
	public void addEvent(String name, ConstructType eventType) {
		super.addEvent(name, eventType);
	}

}
