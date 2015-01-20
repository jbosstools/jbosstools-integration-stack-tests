package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.bpmn2.ui.bot.test.testcase.editor.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Editor tests
//	
//	Everywhere, where is used UserTask
//	@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1183743
//	
//	Where is declared escalation or signal
//	@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1184422
//	
//	temporary workaround used
//	@BZ https://bugzilla.redhat.com/show_bug.cgi?id=1175772
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
	XPathExpressionTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1176400
	LaneTest.class,
	DataObjectTest.class,
	MultipleStartEventTest.class,
	InclusiveSplitTest.class,
	//IntermediateThrowEventNoneTest.class, 
	IntermediateThrowMessageEventTest.class,
	IntermediateThrowEscalationEventTest.class,
	IntermediateCatchSignalSingleTest.class,
	ExclusiveSplitPriorityTest.class,
	EventBasedSplitTest.class, 
	BoundaryEscalationEventOnTaskTest.class,
	ConditionalBoundaryEventInterruptingTest.class, // https://bugzilla.redhat.com/show_bug.cgi?id=1165667
})
public class AllEditorTests extends TestSuite {
	
	static {
		System.setProperty(SWTBotPreferences.KEY_MAX_ERROR_SCREENSHOT_COUNT, "0");
	}
	
}