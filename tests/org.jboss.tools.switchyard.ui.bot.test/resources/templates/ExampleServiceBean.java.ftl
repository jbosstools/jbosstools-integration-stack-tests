package ${package};

import org.switchyard.component.bean.Service;

@Service(ExampleService.class)
public class ExampleServiceBean implements ExampleService {

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

}