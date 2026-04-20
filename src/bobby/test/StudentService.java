package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/student")
public class StudentService {
    @OnStartup(priority = 1)
    public void test1() {
        System.out.println("StudentService: [Startup Priority 1]");
    }

    @OnStartup(priority = 2)
    public void test2() {
        System.out.println("StudentService: [Startup Priority 2]");
    }

    @OnStartup(priority = 3)
    public void test3() {
        System.out.println("StudentService: [Startup Priority 3]");
    }

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
