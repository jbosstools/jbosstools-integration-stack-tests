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
	AdHocSubProcessTest.class,
	AssociationTest.class,
	BooleanStructureReferenceTest.class,
	BusinessRuleTaskTest.class,
	BoundaryConditionalEventOnTaskTest.class,
	ErrorEndEventTest.class,
	ConditionalStartTest.class,
	ImportTest.class, // Headless exception when typing is done! Only on Mac 
	ReceiveTaskTest.class,
	ParallelSplitTest.class,
	RuleTaskTest.class,
//	SendTaskTest.class, // BZ-1079699
//	ErrorBoundaryEventOnTaskTest.class, // BZ-1079714
//	IntermediateCatchEventTimerCycleTest.class, // BZ-1079720
	MessageStartTest.class,
	SubProcessTest.class,
	UserTaskTest.class,
	XPathExpressionTest.class, // @Ignore
	LaneTest.class,
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	IntermediateThrowEventNoneTest.class,
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class,
//	IntermediateCatchSignalSingleTest.class, // BZ-1079720
	ExclusiveSplitPriorityTest.class,
//	EventBasedSplitTest.class, // BZ-1079720
	BoundaryEscalationEventOnTaskTest.class,
	ConditionalBoundaryEventInterruptingTest.class,
})
public class AllEditorTests extends TestSuite {
	
}