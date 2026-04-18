package bobby.test;

import com.varnit.jain.webRock.annotations.*;
import com.varnit.jain.webRock.scope.*;

@Path("/scope")
@InjectSessionScope
@InjectApplicationScope
@InjectRequestScope
@InjectApplicationDirectory
public class ScopeTestService {
    private SessionScope sessionScope;
    private ApplicationScope applicationScope;
    private RequestScope requestScope;
    private ApplicationDirectory applicationDirectory;

    public void setSessionScope(SessionScope sessionScope) {
        this.sessionScope = sessionScope;
    }

    public void setApplicationScope(ApplicationScope applicationScope) {
        this.applicationScope = applicationScope;
    }

    public void setRequestScope(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    public void setApplicationDirectory(ApplicationDirectory applicationDirectory) {
        this.applicationDirectory = applicationDirectory;
    }

    @Path("/set")
    @GET
    public String set() {
        sessionScope.setAttribute("user", "Bobby");
        applicationScope.setAttribute("version", "1.0");
        requestScope.setAttribute("info", "Request-Specific");
        return "Scopes populated!";
    }

    @Path("/get")
    @GET
    public String get() {
        String user = (String) sessionScope.getAttribute("user");
        String version = (String) applicationScope.getAttribute("version");
        String info = (String) requestScope.getAttribute("info");
        String dir = applicationDirectory.getDirectory().getAbsolutePath();

        return "User: " + user + "<br>Version: " + version + "<br>Info: " + info + "<br>App Dir: " + dir;
    }
}
