package ${package};

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public interface Hello {
	
	@Produces("text/plain")
	@Path("/{name}")
	@GET
	String sayHello(@PathParam("name") String name);
	
}
