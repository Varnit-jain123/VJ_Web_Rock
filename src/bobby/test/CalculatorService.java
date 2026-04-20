package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/calculator")
public class CalculatorService {

    @Path("/add")
    @GET
    public String add(@RequestParameter("a") int a, @RequestParameter("b") int b) {
        return "The Sum is: " + (a + b);
    }
}
