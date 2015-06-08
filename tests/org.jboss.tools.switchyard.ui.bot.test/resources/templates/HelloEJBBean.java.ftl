package ${package};

import javax.ejb.Stateless;

import org.switchyard.component.bean.Service;

@Stateless
@Service(Hello.class)
public class HelloBean implements Hello {

	@Override
	public String sayHello(String name) {
		return "${prefix}Hello " + name;
	}

}