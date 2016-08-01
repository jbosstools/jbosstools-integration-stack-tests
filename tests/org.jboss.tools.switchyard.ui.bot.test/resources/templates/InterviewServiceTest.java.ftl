package ${package};

import org.junit.Assert;
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
	@ServiceOperation("InterviewService.verify")
	private Invoker service;

	@Test
	public void testVerify() throws Exception {
		Applicant app1 = service.sendInOut(new Applicant("Twenty", 20)).getContent(Applicant.class);
		Assert.assertTrue(app1.isValid());
		
		Applicant app2 = service.sendInOut(new Applicant("Ten", 10)).getContent(Applicant.class);
		Assert.assertFalse(app2.isValid());
	}

}
