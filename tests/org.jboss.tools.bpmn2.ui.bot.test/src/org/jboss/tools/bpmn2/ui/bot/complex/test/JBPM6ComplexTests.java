package org.jboss.tools.bpmn2.ui.bot.complex.test;


import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAdHocProcessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAdHocSubprocessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAssociationTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBooleanStructureReferenceTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBoundaryConditionalEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBoundaryEscalationEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBusinessRuleTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexCallActivityTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexCompensationEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexConditionalBoundaryEventInterruptingTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexConditionalStartTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexDataObjectTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexErrorBoundaryEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexErrorEndEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexEventBasedSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexExclusiveSplitPriorityTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexImportTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexInclusiveSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateCatchEventTimerCycleTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateCatchSignalSingleTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateThrowEscalationEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateThrowMessageEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexLaneTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMessageStartTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMultiInstanceLoopCharacteristicsTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMultipleStartEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexParalellSplitJoinTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexParalellSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexReceiveTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexSendTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexSubProcessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexUserTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexXPathExpressionTest;
import org.jboss.tools.bpmn2.ui.bot.test.BPMN2Suite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ComplexParalellSplitJoinTest.class,
	ComplexCallActivityTest.class,
	ComplexAdHocProcessTest.class,
	ComplexAdHocSubprocessTest.class,
	ComplexAssociationTest.class,
	ComplexBooleanStructureReferenceTest.class,
	ComplexBusinessRuleTaskTest.class,
	ComplexBoundaryConditionalEventOnTaskTest.class,
	ComplexErrorEndEventTest.class,
	ComplexConditionalStartTest.class,
	ComplexImportTest.class,
	ComplexReceiveTaskTest.class,
	ComplexParalellSplitTest.class,
	ComplexSendTaskTest.class,
	ComplexErrorBoundaryEventOnTaskTest.class,
	ComplexIntermediateCatchEventTimerCycleTest.class,
	ComplexMessageStartTest.class,
	ComplexSubProcessTest.class,
	ComplexUserTaskTest.class,
	ComplexXPathExpressionTest.class,
	ComplexLaneTest.class,
//	// Doesn't work, because of strange context menu appearing during connecting data object with other elements
//	// ComplexDataObjectTest.class, 
	ComplexMultipleStartEventTest.class,
	ComplexInclusiveSplitTest.class,
	ComplexIntermediateThrowMessageEventTest.class,
	ComplexIntermediateThrowEscalationEventTest.class,
	ComplexIntermediateCatchSignalSingleTest.class,
	ComplexExclusiveSplitPriorityTest.class,
	ComplexEventBasedSplitTest.class,
	ComplexBoundaryEscalationEventOnTaskTest.class,
	ComplexConditionalBoundaryEventInterruptingTest.class,
	ComplexMultiInstanceLoopCharacteristicsTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1189454
	ComplexCompensationEvent.class // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1189735
})
@RunWith(BPMN2Suite.class)
public class JBPM6ComplexTests {

}
