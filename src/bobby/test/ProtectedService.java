package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/secure")
@SecuredAccess(checkPost="abc.prq.lmn.SecurityChecker", guard="check")
public class ProtectedService {

    @Path("/data")
    @GET
    public String getData() {
        return "Shhh! This is super secret protected data.";
    }
}
