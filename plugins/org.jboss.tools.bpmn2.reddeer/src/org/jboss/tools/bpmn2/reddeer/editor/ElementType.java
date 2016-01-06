package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.AdHocSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.CallActivity;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ManualTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ReceiveTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SendTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ServiceTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.CompensationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ErrorBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.EscalationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.TimerBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.TimerIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects.DataObject;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.ErrorEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.EventBasedGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.InclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.MessageStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.SignalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.CompensationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.LinkIntermediateThrowEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.MessageIntermediateThrowEvent;
import org.jboss.tools.bpmn2.reddeer.editor.switchyard.activities.SwitchYardServiceTask;

/**
 * 
 */
public enum ElementType {

	PROCESS(null, null),

	AD_HOC_SUB_PROCESS("Sub Processes", "AdHoc Subprocess", "Ad-Hoc Sub-Process", AdHocSubProcess.class),
	SUB_PROCESS("Sub Processes", "Embedded SubProcess", "Sub-Process", SubProcess.class),
	EVENT_SUB_PROCESS("Sub Processes", "Event SubProcess", "Sub-Process"),
	CALL_ACTIVITY("Sub Processes", "Reusable Process", "Call Activity", CallActivity.class),
	MULTIPLE_SUB_PROCESS("Sub Process", "Multiple Instances", "Sub-Process"),

	TASK("Tasks", "Task"),
	MANUAL_TASK("Tasks", "Manual Task", ManualTask.class),
	USER_TASK("Tasks", "User Task", UserTask.class),
	SCRIPT_TASK("Tasks", "Script Task", ScriptTask.class),
	BUSINESS_RULE_TASK("Tasks", "Business Rule Task", BusinessRuleTask.class),
	SERVICE_TASK("Tasks", "Service Task", ServiceTask.class),
	SEND_TASK("Tasks", "Send Task", SendTask.class),
	RECEIVE_TASK("Tasks", "Receive Task", ReceiveTask.class),

	BOUNDARY_EVENT("Boundary Events", "Boundary Event"),
	COMPENSATION_BOUNDARY_EVENT("Boundary Events", "Compensation", CompensationBoundaryEvent.class),
	CONDITIONAL_BOUNDARY_EVENT("Boundary Events", "Conditional", ConditionalBoundaryEvent.class),
	ERROR_BOUNDARY_EVENT("Boundary Events", "Error", ErrorBoundaryEvent.class),
	ESCALATION_BOUNDARY_EVENT("Boundary Events", "Escalation", EscalationBoundaryEvent.class),
	MESSAGE_BOUNDARY_EVENT("Boundary Events", "Message"),
	SIGNAL_BOUNDARY_EVENT("Boundary Events", "Signal"),
	TIMER_BOUNDARY_EVENT("Boundary Events", "Timer", TimerBoundaryEvent.class),

	COMPENSATION_START_EVENT("Start Events", "Compensation"),
	CONDITIONAL_START_EVENT("Start Events", "Conditional", ConditionalStartEvent.class),
	ERROR_START_EVENT("Start Events", "Error"),
	ESCALATION_START_EVENT("Start Events", "Escalation"),
	START_EVENT("Start Events", "Start", StartEvent.class),
	MESSAGE_START_EVENT("Start Events", "Message", MessageStartEvent.class),
	SIGNAL_START_EVENT("Start Events", "Signal", SignalStartEvent.class),
	TIMER_START_EVENT("Start Events", "Timer", TimerStartEvent.class),

	CANCEL_END_EVENT("End Events", "Cancel"),
	COMPENSATION_END_EVENT("End Events", "Compensation"),
	END_EVENT("End Events", "End", EndEvent.class),
	ERROR_END_EVENT("End Events", "Error", ErrorEndEvent.class),
	ESCALATION_END_EVENT("End Events", "Escalation"),
	MESSAGE_END_EVENT("End Events", "Message"),
	SIGNAL_END_EVENT("End Events", "Signal"),
	TERMINATE_END_EVENT("End Events", "Terminate", TerminateEndEvent.class),

	CONDITIONAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Conditional"),
	MESSAGE_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Message"),
	SIGNAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Signal", SignalIntermediateCatchEvent.class),
	TIMER_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Timer", TimerIntermediateCatchEvent.class),

	COMPENSATION_INTERMEDIATE_THROW_EVENT(
			"Intermediate Throw Events",
			"Compensation",
			CompensationIntermediateThrowEvent.class),
	ESCALATION_INTERMEDIATE_THROW_EVENT(
			"Intermediate Throw Events",
			"Escalation",
			EscalationIntermediateThrowEvent.class),
	INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Intermediate"),
	MESSAGE_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Message", MessageIntermediateThrowEvent.class),
	SIGNAL_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Signal"),
	LINK_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Link", LinkIntermediateThrowEvent.class),

	CONDITIONAL_EVENT_DEFINITION("Event Definitions", "Conditional Event Definition"),
	ERROR_EVENT_DEFINITION("Event Definitions", "Error Event Definition"),
	ESCALATION_EVENT_DEFINITION("Event Definitions", "Escalation Event Definition"),
	MESSAGE_EVENT_DEFINITION("Event Definitions", "Message Event Definition"),
	SIGNAL_EVENT_DEFINITION("Event Definitions", "Signal Event Definition"),
	TIMER_EVENT_DEFINITION("Event Definitions", "Timer Event Definition"),

	EXCLUSIVE_GATEWAY("Gateways", "Data-based Exclusive (XOR)", "Exclusive Gateway", ExclusiveGateway.class),
	EVENT_BASED_GATEWAY("Gateways", "Event-based", "Event-Based Gateway", EventBasedGateway.class),
	INCLUSIVE_GATEWAY("Gateways", "Inclusive", "Inclusive Gateway", InclusiveGateway.class),
	PARALLEL_GATEWAY("Gateways", "Parallel", "Parallel Gateway", ParallelGateway.class),

	LANE("Swimlanes", "Lane", Lane.class),

	DATA_OBJECT("Data Objects", "Data Object", DataObject.class),

	TEXT_ANNOTATION("Artifacts", "Text Annotation"),

	SWITCHYARD_SERVICE_TASK("SwitchYard", "SwitchYard Service Task", SwitchYardServiceTask.class);

	private String sectionName;

	private String paletteToolName;

	private boolean container;

	private Class<? extends Element> implClass;

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
		this(sectionName, paletteToolName, paletteToolName, null);
	}

	private ElementType(String sectionName, String paletteToolName, Class<? extends Element> implClass) {
		this(sectionName, paletteToolName, paletteToolName, implClass);
	}

	private ElementType(String sectionName, String paletteToolName, String idOnCanvas) {
		this(sectionName, paletteToolName, idOnCanvas, null);
	}

	private ElementType(String sectionName, String paletteToolName, String idOnCanvas,
			Class<? extends Element> implClass) {
		this.sectionName = sectionName;
		this.paletteToolName = paletteToolName;
		this.idOnCanvas = idOnCanvas;
		this.implClass = implClass;
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
		} else if (sectionName.equals("SwitchYard")) {
			this.idOnCanvas = "Task";
		} else if (sectionName.equals("Sub Processes")) {
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
		return new String[] { sectionName, paletteToolName };
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

	public Class<? extends Element> getJavaClass() {
		if (implClass != null) {
			return implClass;
		} else {
			throw new IllegalStateException(
					"Please provide implementation class for " + paletteToolName + " to constructor of ElementType");
		}
	}
}
