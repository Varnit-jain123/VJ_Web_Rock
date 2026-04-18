package bobby.test;

import com.varnit.jain.webRock.annotations.*;
import com.varnit.jain.webRock.scope.*;

@Path("/autowired")
@InjectSessionScope
public class AutoWiredTestService {
    
    @AutoWired(name="username")
    private String name;

    @AutoWired(name="appVersion")
    private String version;

    private SessionScope sessionScope;

    public void setSessionScope(SessionScope sessionScope) {
        this.sessionScope = sessionScope;
    }

    @Path("/setup")
    @GET
    public String setup() {
        sessionScope.setAttribute("username", "Bobby (from Session)");
        return "Setup complete";
    }

    @Path("/test")
    @GET
    public String test() {
        return "AutoWired Name: " + name + ", Version: " + version;
    }
}
