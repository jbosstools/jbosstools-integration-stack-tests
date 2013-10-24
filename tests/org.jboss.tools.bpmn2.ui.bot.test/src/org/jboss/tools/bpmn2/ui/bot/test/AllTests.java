package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.tools.bpmn2.ui.bot.test.testcase.editor.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Marek Baluch
 */
@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Wizard tests
// ------------
//	ProjectWizardTest.class,
//	ProcessWizardTest.class,
//	Bpmn2ModelWizardTest.class,
//	GenericBpmn2ModelWizardTest.class,
//	JBpmProcessWizardTest.class,
// Editor tests
// ------------
	ParallelSplitJoinTest.class,
	CallActivityTest.class,
	AdHocProcessTest.class,
	AdHocSubProcessTest.class,
	AssociationTest.class,
	BooleanStructureReferenceTest.class,
	BusinessRuleTaskTest.class,
	BoundaryConditionalEventOnTaskTest.class,
	ErrorEndEventTest.class,
	ConditionalStartTest.class,
	ImportTest.class,
	ReceiveTaskTest.class,
	ParallelSplitTest.class,
	RuleTaskTest.class,
	SendTaskTest.class,
	ErrorBoundaryEventOnTaskTest.class,
	IntermediateCatchEventTimerCycleTest.class,
	MessageStartTest.class,
	SubProcessTest.class,
	UserTaskTest.class,
	XPathExpressionTest.class,
	LaneTest.class,
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	IntermediateThrowEventNoneTest.class,
	IntermediateThrowMessageEventTest.class,
//	IntermediateThrowEscalationEventTest.class, // Cannot find Escalation_1
	IntermediateCatchSignalSingleTest.class,
	ExclusiveSplitPriorityTest.class,
	EventBasedSplitTest.class,
//	BoundaryEscalationEventOnTaskTest.class, // Cannot find Escalation_1
//	ConditionalBoundaryEventInterruptingTest.class, // Cannot find Escalation_1 
// Missing features tests
// ----------------------
//  OnEntryExitMixedNamespacedScriptProcessTest.class
})
public class AllTests extends TestSuite {
	// TBD: BPMN2-IntermediateCatchEventTimerCycleWithError.bpmn2
		// error in timer string is not found
}