package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/student")
public class StudentService
{
    @Path("/add")
    @POST
    public String add()
    {
        return "Added";
    }

    @Path("/get")
    @GET
    public String get()
    {
        return "Fetched";
    }

    @Path("/all")
    public String all()
    {
        return "Both allowed";
    }
}
