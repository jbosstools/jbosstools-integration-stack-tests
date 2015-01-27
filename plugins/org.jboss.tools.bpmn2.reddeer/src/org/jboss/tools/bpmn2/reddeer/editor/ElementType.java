package org.jboss.tools.bpmn2.reddeer.editor;

/**
 * 
 */
public enum ElementType {

	PROCESS(null, null),
	
	AD_HOC_SUB_PROCESS("Sub Processes", "AdHoc Subprocess", "Ad-Hoc Sub-Process"),
	SUB_PROCESS("Sub Processes", "Embedded SubProcess", "Sub-Process"),
	EVENT_SUB_PROCESS("Sub Processes", "Event SubProcess", "Sub-Process"),
	CALL_ACTIVITY("Sub Processes", "Reusable Process", "Call Activity"),
	
	
	TASK("Tasks", "Task"),
	MANUAL_TASK("Tasks", "Manual Task"),
	USER_TASK("Tasks", "User Task"),
	SCRIPT_TASK("Tasks", "Script Task"),
	BUSINESS_RULE_TASK("Tasks", "Business Rule Task"),
	SERVICE_TASK("Tasks", "Service Task"),
	SEND_TASK("Tasks", "Send Task"),
	RECEIVE_TASK("Tasks", "Receive Task"),
//	TRANSACTION("TBD", "Transaction"),
	
	BOUNDARY_EVENT("Boundary Events", "Boundary Event"),
	COMPENSATION_BOUNDARY_EVENT("Boundary Events", "Compensation Boundary Event"),
	CONDITIONAL_BOUNDARY_EVENT("Boundary Events", "Conditional"),
	ERROR_BOUNDARY_EVENT("Boundary Events", "Error"),
	ESCALATION_BOUNDARY_EVENT("Boundary Events", "Escalation"),
	MESSAGE_BOUNDARY_EVENT("Boundary Events", "Message"),
	SIGNAL_BOUNDARY_EVENT("Boundary Events", "Signal"),
	TIMER_BOUNDARY_EVENT("Boundary Events", "Timer"),
	
	COMPENSATION_START_EVENT("Start Events", "Compensation"),
	CONDITIONAL_START_EVENT("Start Events", "Conditional"),
	ERROR_START_EVENT("Start Events", "Error"),
	ESCALATION_START_EVENT("Start Events", "Escalation"),
	START_EVENT("Start Events","Start"),
	MESSAGE_START_EVENT("Start Events", "Message"),
	SIGNAL_START_EVENT("Start Events", "Signal"),
	TIMER_START_EVENT("Start Events", "Timer"),
	
	CANCEL_END_EVENT("End Events", "Cancel"),
	COMPENSATION_END_EVENT("End Events", "Compensation"),
	END_EVENT("End Events", "End"),
	ERROR_END_EVENT("End Events", "Error"),
	ESCALATION_END_EVENT("End Events", "Escalation"),
	MESSAGE_END_EVENT("End Events", "Message"),
	SIGNAL_END_EVENT("End Events", "Signal"),
	TERMINATE_END_EVENT("End Events", "Terminate"),
	
	CONDITIONAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Conditional"),
	MESSAGE_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Message"),
	SIGNAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Signal"),
	TIMER_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Timer"),
	
	COMPENSATION_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Compensation"),
	ESCALATION_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Escalation"),
	INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Intermediate"),
	MESSAGE_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Message"),
	SIGNAL_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Signal"),
	
	CONDITIONAL_EVENT_DEFINITION("Event Definitions", "Conditional Event Definition"),
	ERROR_EVENT_DEFINITION("Event Definitions", "Error Event Definition"),
	ESCALATION_EVENT_DEFINITION("Event Definitions", "Escalation Event Definition"),
	MESSAGE_EVENT_DEFINITION("Event Definitions", "Message Event Definition"),
	SIGNAL_EVENT_DEFINITION("Event Definitions", "Signal Event Definition"),
	TIMER_EVENT_DEFINITION("Event Definitions", "Timer Event Definition"),
	
	EXCLUSIVE_GATEWAY("Gateways", "Data-based Exclusive (XOR)", "Exclusive Gateway"),
	EVENT_BASED_GATEWAY("Gateways", "Event-based", "Event-Based Gateway"),
	INCLUSIVE_GATEWAY("Gateways", "Inclusive", "Inclusive Gateway"),
	PARALLEL_GATEWAY("Gateways", "Parallel", "Parallel Gateway"),
	
	LANE("Swimlanes", "Lane"),

	DATA_OBJECT("Data Objects", "Data Object"),
	
	TEXT_ANNOTATION("Artifacts", "Text Annotation"), 
	
	SWITCHYARD_SERVICE_TASK("SwitchYard", "SwitchYard Service Task");

	private String sectionName;
	
	private String paletteToolName;
	
	private boolean container;
	
	/**
	 * This string uses class {@link ConstructOfType} for matching elements on canvas
	 */
	private String idOnCanvas;
	
	/**
	 * 
	 * @param sectionName
	 * @param paletteToolName
	 */
	private ElementType(String sectionName, String paletteToolName) {
		this(sectionName, paletteToolName, paletteToolName);
	}
	
	private ElementType(String sectionName, String paletteToolName, String idOnCanvas) {
		this.sectionName = sectionName;
		this.paletteToolName = paletteToolName;
		this.idOnCanvas = idOnCanvas;
		this.container = false;
		
		if (sectionName == null || paletteToolName == null) {
			this.idOnCanvas = "Process";
		} else if (sectionName.equals("End Events")) {
			this.idOnCanvas = "End Event";
		} else if (sectionName.equals("Start Events")) {
			this.idOnCanvas = "Start Event";
		} else if (sectionName.equals("Boundary Events")) {
			this.idOnCanvas = "Boundary Event";
		} else if (sectionName.equals("Intermediate Catch Events")) {
			this.idOnCanvas = "Intermediate Catch Event";
		} else if (sectionName.equals("Intermediate Throw Events")) {
			this.idOnCanvas = "Intermediate Throw Event";
		} else if (sectionName.equals("SwitchYard")){
			this.idOnCanvas = "Task";
		} else if(sectionName.equals("Sub Processes")) {
			this.container = true;
		}
		
		this.idOnCanvas = this.idOnCanvas.replace(" ", "").replace("-", "");
	}

	public String getSectionName() {
		return sectionName;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isElementContainer() {
		return AD_HOC_SUB_PROCESS.equals(this) || SUB_PROCESS.equals(this) || PROCESS.equals(this);
	}
	
	/**
	 * Returns the name of the type. E.g. PARALLEL_GATEWAY -> Parallel Gateway. 
	 * 
	 * @return
	 */
	public String toToolName() {
		return paletteToolName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] toToolPath() {
		return new String[] {sectionName, paletteToolName};
	}

	/**
	 * 
	 * @return
	 */
	public String getIdOnCanvas() {
		return idOnCanvas;
	}
	
	public boolean isContainer() {
		return container;
	}
}
