package ${package};

import org.switchyard.component.bean.Service;

@Service(Info.class)
public class InfoBean implements Info {

	@Override
	public void printInfo(String body) {
		System.out.println("Body: " + body);
	}

}