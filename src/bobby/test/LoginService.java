package bobby.test;

import com.varnit.jain.webRock.annotations.*;
import com.varnit.jain.webRock.scope.*;

@Path("/login")
public class LoginService {

    @Path("/doLogin")
    @GET
    public String login(SessionScope ss) {
        ss.setAttribute("token", "VALID");
        return "Logged In Successfully!";
    }

    @Path("/logout")
    @GET
    public String logout(SessionScope ss) {
        ss.setAttribute("token", null);
        return "Logged Out!";
    }
}
