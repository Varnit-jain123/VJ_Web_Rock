package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/parameter")
public class ParameterTestService {

    @Path("/charTest")
    @GET
    public String charTest(@RequestParameter("grade") char g) {
        return "Grade: " + g;
    }

    @Path("/calc")
    @GET
    public String calculate(@RequestParameter("a") int a, @RequestParameter("b") int b) {
        return "Sum is: " + (a + b);
    }

    @Path("/bool")
    @GET
    public String booleanTest(@RequestParameter("active") boolean active) {
        return "Active status: " + active;
    }

    @Path("/string")
    @GET
    public String stringTest(@RequestParameter("name") String name) {
        return "Hello, " + name;
    }
}
