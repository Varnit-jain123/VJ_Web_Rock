package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/student")
public class StudentService {
    @Path("/add")
    @POST
    @FORWARD("/student/list")
    public String add() {
        return "Added (this should not be seen)";
    }

    @Path("/details")
    @GET
    @FORWARD("/details.jsp")
    public String details() {
        return "Details (this should not be seen)";
    }

    @Path("/list")
    @GET
    public String list() {
        return "Listing students (Internal Forward Success)";
    }
}
