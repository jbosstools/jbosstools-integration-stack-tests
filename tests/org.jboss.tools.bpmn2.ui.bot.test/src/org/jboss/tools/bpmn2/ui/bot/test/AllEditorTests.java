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
//  @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1157656
	ParallelSplitJoinTest.class,
	CallActivityTest.class,
	AdHocProcessTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1147940
	AdHocSubProcessTest.class,
	AssociationTest.class,
	BooleanStructureReferenceTest.class, //@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1151984
	BusinessRuleTaskTest.class,
	BoundaryConditionalEventOnTaskTest.class,
	ErrorEndEventTest.class,
	ConditionalStartTest.class,
	ImportTest.class, // Headless exception when typing is done! Only on Mac 
					  // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1155145
	ReceiveTaskTest.class, 
	ParallelSplitTest.class,
	RuleTaskTest.class,
	SendTaskTest.class, 
	ErrorBoundaryEventOnTaskTest.class,
	IntermediateCatchEventTimerCycleTest.class,
	MessageStartTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1155707
	SubProcessTest.class,
	UserTaskTest.class,
	XPathExpressionTest.class, // @Ignore
	LaneTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1156314
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	//IntermediateThrowEventNoneTest.class, 
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class, // https://bugzilla.redhat.com/show_bug.cgi?id=1162174
	IntermediateCatchSignalSingleTest.class,
	ExclusiveSplitPriorityTest.class,
	EventBasedSplitTest.class, 
	BoundaryEscalationEventOnTaskTest.class, // https://bugzilla.redhat.com/show_bug.cgi?id=1165807
	ConditionalBoundaryEventInterruptingTest.class, // https://bugzilla.redhat.com/show_bug.cgi?id=1165667
})
public class AllEditorTests extends TestSuite {
	
	static {
		System.setProperty(SWTBotPreferences.KEY_MAX_ERROR_SCREENSHOT_COUNT, "0");
	}
	
}