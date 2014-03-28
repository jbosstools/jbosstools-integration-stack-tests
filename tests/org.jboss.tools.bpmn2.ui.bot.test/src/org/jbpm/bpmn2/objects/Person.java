package org.jbpm.bpmn2.objects;

/**
 * Class used in editor tests.
 */
public class Person {

	private String name;
	
	/**
	 * 
	 */
	public Person() {
	}
	
	/**
	 * 
	 * @param name
	 */
	public Person(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
}
