package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.bpmn2.ui.bot.test.testcase.editor.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

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
	SendTaskTest.class, // BZ-1079699
	ErrorBoundaryEventOnTaskTest.class, // BZ-1079714 // Untested
	IntermediateCatchEventTimerCycleTest.class, // BZ-1079720 // Untested
	MessageStartTest.class,
	SubProcessTest.class,
	UserTaskTest.class,
	XPathExpressionTest.class, // @Ignore
	LaneTest.class,
	DataObjectTest.class,
	MultipleStartEventTest.class, // BZ-1085520
	InclusiveSplitTest.class,
	IntermediateThrowEventNoneTest.class,
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class,
	IntermediateCatchSignalSingleTest.class, // BZ-1079720 // Untested
	ExclusiveSplitPriorityTest.class,
	EventBasedSplitTest.class, // BZ-1079720 // Untested
	BoundaryEscalationEventOnTaskTest.class,
	ConditionalBoundaryEventInterruptingTest.class,
})
public class AllEditorTests extends TestSuite {
	
	static {
		System.setProperty(SWTBotPreferences.KEY_MAX_ERROR_SCREENSHOT_COUNT, "0");
	}
	
}