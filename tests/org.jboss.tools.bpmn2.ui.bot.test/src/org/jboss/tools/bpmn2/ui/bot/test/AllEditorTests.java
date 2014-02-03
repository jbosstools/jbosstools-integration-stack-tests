package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.tools.bpmn2.ui.bot.test.testcase.editor.*;import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Marek Baluch
 */
@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Editor tests
// ------------
	// Uncovered:
	// 	BZ-1053789
	ParallelSplitJoinTest.class,
	CallActivityTest.class,
	AdHocProcessTest.class,
	AdHocSubProcessTest.class, // BZ-1053781
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
	SendTaskTest.class, // BZ-1053825
	ErrorBoundaryEventOnTaskTest.class,
	IntermediateCatchEventTimerCycleTest.class,
	MessageStartTest.class,
	SubProcessTest.class, // BZ-1053781
	UserTaskTest.class,
	XPathExpressionTest.class, // @Ignore
	LaneTest.class, // BZ-1053784
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	IntermediateThrowEventNoneTest.class,
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class,
	IntermediateCatchSignalSingleTest.class,
	ExclusiveSplitPriorityTest.class,
	EventBasedSplitTest.class,
	BoundaryEscalationEventOnTaskTest.class,
	ConditionalBoundaryEventInterruptingTest.class, // BZ-1053781
})
public class AllEditorTests extends TestSuite {
}