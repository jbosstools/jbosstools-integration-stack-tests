package ${package};

import org.switchyard.component.bean.Service;

@Service(Hello.class)
public class HelloBean implements Hello {

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

}