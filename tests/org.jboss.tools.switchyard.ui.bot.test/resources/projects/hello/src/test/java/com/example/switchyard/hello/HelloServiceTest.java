package com.example.switchyard.hello;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = {
	CDIMixIn.class,
	HTTPMixIn.class })
public class HelloServiceTest {

	private HTTPMixIn httpMixIn;

	@BeforeDeploy
	public void setProperties() {
		System.setProperty("org.switchyard.component.http.standalone.port", "8081");
	}

	@Test
	public void testSayHello() throws Exception {
		String result = httpMixIn.sendString("http://localhost:8081/hello", "Johnny Cash", HTTPMixIn.HTTP_POST);
		assertEquals("Hello Johnny Cash", result);
	}

}
