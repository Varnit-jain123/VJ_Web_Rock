package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/admin")
@GET
public class AdminService
{
    @Path("/list")
    public String list()
    {
        return "Only GET allowed";
    }

    @Path("/update")
    @POST
    public String update()
    {
        return "POST only (overridden)";
    }
}
