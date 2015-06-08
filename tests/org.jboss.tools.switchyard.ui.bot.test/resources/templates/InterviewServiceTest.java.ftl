package ${package};

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class })
public class InterviewServiceTest {

	private SwitchYardTestKit testKit;
	private CDIMixIn cdiMixIn;
	@ServiceOperation("InterviewService")
	private Invoker service;

	@Test
	public void testVerify() throws Exception {
		Applicant message = new Applicant("Twenty", 20);
		service.operation("verify").sendInOnly(message);
		service.operation("verify").sendInOnly(message);
		message = new Applicant("Ten", 10);
	}

}
