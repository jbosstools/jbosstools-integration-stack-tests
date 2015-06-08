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
public class ExampleServiceTest {

	private SwitchYardTestKit testKit;
	private CDIMixIn cdiMixIn;
	@ServiceOperation("ExampleService")
	private Invoker service;

	@Test
	public void sayHelloTest() throws Exception {
		String result = service.operation("sayHello").sendInOut("Andrej").getContent(String.class);
		Assert.assertEquals("Hello Andrej", result);
	}
}