package ${package};


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author apodhrad
 *
 */
public class MyRouteBuilder extends RouteBuilder {

	public void configure() {
		from("switchyard://${from}").streamCaching()
				.log("Received message '${body}'")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String body = exchange.getIn().getBody(String.class);
						exchange.getOut().setBody("Hello " + body.trim());
						exchange.getOut().setHeaders(exchange.getIn().getHeaders());
					}
				})
				.log("Output message '${body}'");
	}

}
