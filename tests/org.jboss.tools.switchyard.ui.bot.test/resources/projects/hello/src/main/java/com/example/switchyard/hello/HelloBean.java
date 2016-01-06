package com.example.switchyard.hello;

import org.switchyard.component.bean.Service;

@Service(Hello.class)
public class HelloBean implements Hello {

	@Override
	public String sayHello(Person person) {
		return "Hello " + person.getName();
	}

}
