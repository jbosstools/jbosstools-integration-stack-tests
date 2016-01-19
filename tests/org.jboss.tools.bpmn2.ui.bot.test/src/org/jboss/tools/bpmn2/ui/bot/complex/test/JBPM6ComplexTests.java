package org.jboss.tools.bpmn2.ui.bot.complex.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAdHocProcessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAdHocSubprocessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexAssociationTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBooleanStructureReferenceTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBoundaryConditionalEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBoundaryEscalationEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexBusinessRuleTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexCallActivityTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexCompensationEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexConditionalStartTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexDataObjectTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexDefaultSkippableTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexDefinitionViaElementTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexErrorBoundaryEventOnTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexErrorEndEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexEventBasedSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexExclusiveSplitPriorityTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexImportInterfaceTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexImportTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexInclusiveSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateCatchEventTimerCycleTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateCatchSignalSingleTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateThrowEscalationEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexIntermediateThrowMessageEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexLaneTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexLinkEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexManualTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMessageStartTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMultiInstanceLoopCharacteristicsTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexMultipleStartEventTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexParalellSplitJoinTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexParalellSplitTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexReceiveTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexSendTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexServiceTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexSpecialCharactersAndLenghtsTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexSubProcessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexUserTaskTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexWebDesignerProcessTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexXPathExpressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ComplexAdHocProcessTest.class,
	ComplexAdHocSubprocessTest.class,
	ComplexAssociationTest.class,
	ComplexBooleanStructureReferenceTest.class,
	ComplexBoundaryConditionalEventOnTaskTest.class,
	ComplexBoundaryEscalationEventOnTaskTest.class,
	ComplexBusinessRuleTaskTest.class,
	ComplexCallActivityTest.class,
	ComplexCompensationEventTest.class, // https://bugzilla.redhat.com/show_bug.cgi?id=1209449
	ComplexConditionalStartTest.class,
	ComplexDataObjectTest.class,
	ComplexDefaultSkippableTest.class,
	ComplexDefinitionViaElementTest.class,
	ComplexErrorBoundaryEventOnTaskTest.class,
	ComplexErrorEndEventTest.class,
	ComplexEventBasedSplitTest.class,
	ComplexExclusiveSplitPriorityTest.class,
	ComplexImportInterfaceTest.class,
	ComplexImportTest.class,
	ComplexInclusiveSplitTest.class,
	ComplexIntermediateCatchEventTimerCycleTest.class,
	ComplexIntermediateCatchSignalSingleTest.class,
	ComplexIntermediateThrowEscalationEventTest.class,
	ComplexIntermediateThrowMessageEventTest.class,
	ComplexLaneTest.class,
	ComplexLinkEventTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1190688
	ComplexManualTaskTest.class,
	ComplexMessageStartTest.class,
	ComplexMultiInstanceLoopCharacteristicsTest.class,
	ComplexMultipleStartEventTest.class,
	ComplexParalellSplitJoinTest.class,
	ComplexParalellSplitTest.class,
	ComplexReceiveTaskTest.class, // @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1188592
	ComplexSendTaskTest.class,
	ComplexServiceTaskTest.class,
	ComplexSpecialCharactersAndLenghtsTest.class,
	ComplexSubProcessTest.class,
	ComplexUserTaskTest.class,
	ComplexWebDesignerProcessTest.class,
	ComplexXPathExpressionTest.class,
})
@RunWith(RedDeerSuite.class)
public class JBPM6ComplexTests {

}
