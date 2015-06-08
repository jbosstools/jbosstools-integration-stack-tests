package ${package};

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class })
public class HelloTest {

	@ServiceOperation("Hello")
	private Invoker service;

	@Test
	public void testSayHello() throws Exception {
		String message = "World";
		String result = service.operation("sayHello").sendInOut(message).getContent(String.class);
		Assert.assertEquals("Hello World", result);
	}

}
