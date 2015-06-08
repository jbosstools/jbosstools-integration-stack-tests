package ${package};

import org.apache.camel.builder.RouteBuilder;

public class Proxy extends RouteBuilder {

	public void configure() {
		from("switchyard://${from}").log("Received message for 'HelloRef' : ${body}").to("switchyard://${to}");
	}

}
