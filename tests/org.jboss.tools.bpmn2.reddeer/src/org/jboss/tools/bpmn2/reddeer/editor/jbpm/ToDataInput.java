package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;

/**
 * Represents the target side of parameter mapping.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ToDataInput implements MappingSide {
	
	private String name;
	
	private String dataType;
	
	/**
	 * Creates a new instance of ToDataOutput.
	 * 
	 * @param name
	 */
	public ToDataInput(String name) {
		this(name, null);
	}
	
	/**
	 * Creates a new instance of ToDataOutput.
	 * 
	 * @param name
	 * @param dataType
	 */
    public ToDataInput(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	
    /**
     * Define new data output.
     */
    public void add() {
    	/*
    	 * Use DataInput class as the steps required to define
    	 * a new data output are the same.
    	 */
    	new FromDataOutput(name, dataType).add();
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
