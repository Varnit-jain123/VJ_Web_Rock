package bobby.test;

import com.varnit.jain.webRock.annotations.*;

@Path("/injection")
public class InjectionTestService {

    @InjectRequestParameter("name")
    private String name;

    @InjectRequestParameter("age")
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Path("/print")
    @GET
    public String print() {
        return "Name: " + name + ", Age: " + age;
    }
}
