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
//	// Uncovered:
//	// 	BZ-1053789
	ParallelSplitJoinTest.class,
	CallActivityTest.class,
	AdHocProcessTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1147940
	AdHocSubProcessTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1153075
	AssociationTest.class,
	BooleanStructureReferenceTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1151984
	BusinessRuleTaskTest.class,
	BoundaryConditionalEventOnTaskTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1153008
	ErrorEndEventTest.class,
	ConditionalStartTest.class,
	ImportTest.class, // Headless exception when typing is done! Only on Mac
	ReceiveTaskTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1134874 
	ParallelSplitTest.class,
	RuleTaskTest.class,
	SendTaskTest.class, 
	ErrorBoundaryEventOnTaskTest.class,
	IntermediateCatchEventTimerCycleTest.class,
	MessageStartTest.class,
	SubProcessTest.class,
	UserTaskTest.class,
	XPathExpressionTest.class, // @Ignore
	LaneTest.class,
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	//IntermediateThrowEventNoneTest.class, 
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class,
	IntermediateCatchSignalSingleTest.class,
	ExclusiveSplitPriorityTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1140542
	EventBasedSplitTest.class, 
	BoundaryEscalationEventOnTaskTest.class,
	ConditionalBoundaryEventInterruptingTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1153008
})
public class AllEditorTests extends TestSuite {
	
	static {
		System.setProperty(SWTBotPreferences.KEY_MAX_ERROR_SCREENSHOT_COUNT, "0");
	}
	
}