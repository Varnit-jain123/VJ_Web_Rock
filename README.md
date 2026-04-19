# VJWebRock Framework

**VJWebRock** is a lightweight, high-performance Java Web Framework designed to simplify server-side development with a modern, annotation-driven approach.

### Industry Standard Patterns
VJWebRock implements proven architectural patterns used by industry-leading frameworks:
- **Front Controller Pattern**: Similar to **Spring MVC** and **Struts 2**.
- **Dependency Injection (DI)**: Inspired by the **Spring Framework** and **Google Guice**.
- **Annotation Routing**: Comparable to **JAX-RS (Jersey)** and **Spring Boot**.
- **Automated SDK Generation**: Mirrors the functionality of **OpenAPI/Swagger** and **GraphQL** codegen tools.

---

##  Key Features

### 1.  Intelligent Routing
- **Annotation Based**: Use `@Path("/url")` on classes and methods to define clear, hierarchical routing.
- **HTTP Method Support**: Explicitly control access with `@GET` and `@POST` annotations.
- **Internal Forwarding**: Simple redirection using the `@FORWARD("/target")` annotation.

### 2.  Powerful Dependency Injection
- **Scope Injection**: Inject `RequestScope`, `SessionScope`, and `ApplicationScope` directly into methods or class fields.
- **Auto-Wiring**: Use `@AutoWired(name="...")` to automatically pull objects from any scope into your service fields.
- **Parameter Injection**: Seamlessly map request parameters to method arguments or class fields using `@RequestParameter` and `@InjectRequestParameter`.

### 3.  Automated POJO & JSON Binding
- **Request Body Parsing**: Automatically converts incoming JSON request bodies into Java POJO objects using GSON.
- **Response Serialization**: Automatically serializes Java objects returned by methods into formatted JSON responses.

### 4.  Startup Lifecycle
- **Hooks**: Use `@OnStartup` to execute code as soon as the framework initializes.
- **Priority**: Support for prioritized execution to ensure dependencies are initialized in the correct order.

### 5.  Database Connectivity (MySQL)
- **JDBC Integration**: Seamlessly connect to MySQL databases within your service methods.
- **CRUD Operations**: Support for standard Create, Read, Update, and Delete operations using JDBC.


### 6.  Security Layer
- **Guards**: Secure your service classes with `@SecuredAccess`, specifying a `Guard` class and method to validate requests before they reach your logic.

### 7.  JavaScript Proxy SDK
- **Zero-Config Client**: The framework automatically generates a JavaScript SDK (`vj-webrock.js`) that mirrors your server-side services.
- **Easy Integration**: Call server-side methods as if they were local JavaScript functions with full `fetch` and `Promise` support.

---

## 📈 The Evolution of VJWebRock

Following a rigorous architectural timeline, the framework has evolved through 13 distinct development phases:

| Phase | Milestone | Description |
| :--- | :--- | :--- |
| **Initial** | **Core Foundation** | Established Reflection-based annotation processing and basic class loading. |
| **2nd** | **Basic Routing** | Implemented the Front Controller and mapped paths to Controller classes. |
| **3rd** | **Forwarding** | Added the `@FORWARD` annotation for seamless server-side redirection. |
| **4th** | **Lifecycle Hooks** | Introduced `@OnStartup` for prioritized initialization logic. |
| **5th** | **Scope Injection** | Dedicated support for Request, Session, and Application scope management. |
| **6th** | **Auto-Wiring** | Built the Dependency Injection engine using the `@AutoWired` annotation. |
| **7th** | **Method Binding** | Mapping of request parameters directly to method arguments. |
| **9th** | **Class Binding** | Intelligent `@InjectRequestParameter` at the class field level. |
| **10th** | **JSON Inbound** | Integrated GSON for automated request body to POJO conversion. |
| **11th** | **Security Layer** | Multi-level security using `@SecuredAccess` and custom `Guard` classes. |
| **12th** | **JSON Outbound** | Automated serialization of Java return types to standard JSON responses. |
| **13th** | **Proxy SDK Gen** | **Zero-Config JavaScript Proxy SDK Generation for client-side integration.** |

---

##  The "Bobby" Validation Rules 

When developing services within the `bobby` package (or your configured service package), the following **"Validation Bobby"** rules must be strictly applied to ensure framework stability:

1.  **Public Access**: All service classes and mapped methods must be `public`.
2.  **OnStartup Integrity**: Methods annotated with `@OnStartup` must have `void` return type and **exactly zero** parameters.
3.  **Setter Requirement**: Any field annotated with `@AutoWired` or `@InjectRequestParameter` **MUST** have a corresponding public setter method following standard Java Bean naming conventions (e.g., `setName(String name)` for a field `name`).
4.  **Unique Paths**: Multiple services cannot map to the same URL path. The framework will report a duplicate URL error if detected.
5.  **Single JSON Body**: A service method can have at most **one** parameter that is treated as the JSON request body (any parameter not annotated or recognized as a Scope).
6.  **Path Formatting**: All `@Path` values should follow a consistent slash-prefixing convention (e.g., `@Path("/student")`).

---

##  Documentation Tool: ServicesDoc

We provide a standalone CLI tool, **ServicesDoc**, to analyze your compiled application and generate professional PDF documentation.

###  What it does:
- Scans `.class` files to map all service endpoints.
- Identifies dependency injections and scope mappings.
- Visualizes security configurations.
- **Performs Validation Checks**: Reports missing setters, invalid startup hooks, and duplicate mappings.

> [!NOTE]
> For detailed instructions on how to build and run the documentation tool, please refer to the [ServicesDoc README](file:///c:/tomcat9/webapps/VJ_web_rock/ServicesDoc/README.md).

---

##  Database Integration (MySQL)

VJWebRock simplifies database interaction by allowing you to integrate MySQL JDBC drivers directly into your services. 

###  Configuration
1. Add `mysql-connector-java.jar` to your `WEB-INF/lib` folder.
2. Initialize the connection within your service or use a utility class:

```java
private Connection getConnection() throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/YourDB",
        "username",
        "password"
    );
}
```

###  Example CRUD Service
```java
@Path("/data")
public class DataService {
    @Path("/add")
    @POST
    public String add(Pojo obj) {
        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO table VALUES(?,?)");
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getName());
            ps.executeUpdate();
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
```

---

##  Getting Started

1.  **Project Structure**: Place your services in the package defined in `web.xml` .
2.  **Deployment**: Deploy the project to **Tomcat 9**.
3.  **SDK Access**: Visit `/js/` or the configured JS route to download your auto-generated Proxy SDK.
4.  **Documentation**: Run `ServicesDoc.jar` against your `WEB-INF/classes` folder to generate your API manual.

---

*Built by Varnit Jain*
