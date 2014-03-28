package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * 
 */
public class ParameterMapping implements org.jboss.tools.bpmn2.reddeer.editor.ParameterMapping {

	public enum Type {
	    INPUT, OUTPUT;
	
	    public String label() {
	        return name().charAt(0) + name().substring(1).toLowerCase();
	    }
	}

	private MappingSide from;
	private MappingSide to;
	private Type type;

	/**
	 * 
	 * @param from
	 * @param to
	 * @param type
	 */
	public ParameterMapping(MappingSide from, MappingSide to, Type type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public MappingSide getFrom() {
		return from;
	}

	/**
	 * 
	 * @return
	 */
	public MappingSide getTo() {
		return to;
	}

	/**
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}
	
	@Override
	public void setUp() {
		from.setUp();
		to.setUp();
	}
	
}
