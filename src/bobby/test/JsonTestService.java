package bobby.test;

import com.varnit.jain.webRock.annotations.*;
import com.varnit.jain.webRock.scope.*;

@Path("/jsonTest")
public class JsonTestService {

    @Path("/add")
    @POST
    public String add(Student s) {
        return "Received Student: " + s.getName();
    }

    @Path("/save")
    @POST
    public String save(Student s, SessionScope ss) {
        ss.setAttribute("lastStudent", s);
        return "Student " + s.getName() + " saved to session!";
    }

    @Path("/checkSession")
    @GET
    public String check(SessionScope ss) {
        Student s = (Student) ss.getAttribute("lastStudent");
        if (s == null)
            return "No student in session";
        return "Session Student: " + s.getName();
    }
}
