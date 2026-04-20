# VJWebRock Framework

**VJWebRock** is a lightweight, high-performance Java web framework designed to simplify server-side development using a modern, annotation-driven approach. It incorporates proven architectural patterns such as Front Controller, Dependency Injection, and automated SDK generation, similar to frameworks like Spring MVC and JAX-RS.

This repository is structured as a professional framework library along with an integrated demo application.

---

## Repository Structure

The project is organized into the following components:

- **VJWebRock-Core**
  - Contains the core framework library
  - `src/`: Framework source code (annotations, scopes, JSON binding)
  - `make-jar.bat`: Script to build the `vj-webrock.jar`

- **Demo Application (Integrated)**
  - `src/`: Example service implementations and business logic
  - `web/`: Web assets (HTML, JS, JSP) and `WEB-INF`
  - Root directory acts as the deployment root for Tomcat

- **ServicesDoc**
  - CLI tool for generating professional API documentation (PDF)

---

## Industry-Standard Patterns

VJWebRock implements widely adopted architectural patterns:

- Front Controller Pattern (similar to Spring MVC and Struts 2)
- Dependency Injection (inspired by Spring and Google Guice)
- Annotation-Based Routing (similar to JAX-RS and Spring Boot)
- Automated SDK Generation (similar to OpenAPI/Swagger and GraphQL tools)

---

## Key Features

### Intelligent Routing
- Define routes using `@Path("/url")` at class and method levels
- Control HTTP access using `@GET` and `@POST`
- Perform internal forwarding using `@FORWARD("/target")`

### Dependency Injection
- Inject `RequestScope`, `SessionScope`, and `ApplicationScope`
- Use `@AutoWired(name="...")` for automatic dependency resolution
- Map request parameters using `@RequestParameter` and `@InjectRequestParameter`

### POJO and JSON Binding
- Automatically convert JSON request bodies into Java POJOs using GSON
- Automatically serialize Java responses into JSON

### JavaScript Proxy SDK
- Automatically generates `vj-webrock.js`
- Allows calling backend services like local JavaScript functions

### Security and Lifecycle
- Secure services using `@SecuredAccess` with custom Guard classes
- Execute startup logic using `@OnStartup` with priority support

### Database Integration (MySQL)
- Direct JDBC integration
- Full CRUD operation support

### Bobby-Powered Validation
- Pre-compilation validation layer ensuring strict framework rules
- Prevents invalid configurations before deployment

---

## Getting Started

### 1. Build the Framework
Navigate to `VJWebRock-Core` and run:
```bash
make-jar.bat
```

---

### 2. Compile the Application
From the project root, run:

**Command Prompt**
```bash
compile
```

**PowerShell**
```bash
.\compile
```

---

### 3. Run on Tomcat 9
Deploy the project to Tomcat:

- URL: `http://localhost:8080/VJ_web_rock/CalculatorDemo.html`
- SDK Access: `/js/vj-webrock.js`

---

## Web Configuration (web.xml)

VJWebRock uses a Front Controller architecture with supporting startup and utility servlets.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
  version="4.0">

    <description>
        VJWebRock implementation
    </description>
    <display-name>VJWebRock</display-name>

    <servlet>
        <servlet-name>VJWebRock</servlet-name>
        <servlet-class>com.varnit.jain.webRock.VJWebRock</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>VJWebRock</servlet-name>
        <url-pattern>/schoolService/*</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>VJWebRockStarter</servlet-name>
        <servlet-class>com.varnit.jain.webRock.VJWebRockStarter</servlet-class>
        <init-param>
            <param-name>SERVICE_PACKAGE_PREFIX</param-name>
            <param-value>bobby</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet>
        <servlet-name>JSGenerator</servlet-name>
        <servlet-class>com.varnit.jain.webRock.JSGeneratorServlet</servlet-class>
        <load-on-startup>2</load-on-startup>
    </servlet>

    <servlet>
        <servlet-name>JSLoader</servlet-name>
        <servlet-class>com.varnit.jain.webRock.JSLoaderServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>JSLoader</servlet-name>
        <url-pattern>/js/*</url-pattern>
    </servlet-mapping>

</web-app>
```

---

## Configuration Validation Rules

### Required Parameter

- `SERVICE_PACKAGE_PREFIX`
  - Defines the base package containing service classes

---

### Validation Rules

1. Parameter must be present in `VJWebRockStarter`
2. Value must not be null or empty
3. Must follow valid Java package naming conventions
4. Package must exist in `WEB-INF/classes`
5. Startup order must be correct (`VJWebRockStarter` before others)

---

### Optional Configuration

```xml
<context-param>
    <param-name>JsFile</param-name>
    <param-value>custom-sdk.js</param-value>
</context-param>
```

Default file: `*.js`
Save in different files as per requirement.

---

## Bobby Validation Rules

1. All service classes and methods must be `public`
2. `@OnStartup` methods must return `void` and have zero parameters
3. Fields with `@AutoWired` or `@InjectRequestParameter` must have public setters
4. URL paths must be unique
5. Only one JSON body parameter per method
6. All `@Path` values must follow consistent formatting

---

## Evolution of VJWebRock

| Phase   | Milestone              | Description |
|--------|----------------------|------------|
| Initial | Core Foundation       | Reflection-based annotation processing |
| 2nd     | Basic Routing         | Front Controller and URL mapping |
| 3rd     | Forwarding            | Introduced `@FORWARD` |
| 4th     | Lifecycle Hooks       | Introduced `@OnStartup` |
| 5th     | Scope Injection       | Request, Session, Application scopes |
| 6th     | Auto-Wiring           | Dependency Injection engine |
| 7th     | Method Binding        | Parameter-to-method mapping |
| 9th     | Class Binding         | Field-level injection |
| 10th    | JSON Inbound          | JSON to POJO conversion |
| 11th    | Security Layer        | Guard-based access control |
| 12th    | JSON Outbound         | JSON serialization |
| 13th    | Proxy SDK Generation  | JavaScript SDK generation |

---

## Documentation Tool: ServicesDoc

ServicesDoc is a CLI tool that:

- Scans compiled `.class` files
- Maps service endpoints
- Detects dependency injections and scopes
- Validates configuration issues
- Generates professional PDF documentation

---

## Database Configuration Example

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

---

## Example CRUD Service

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

## Notes

- Place services in the package defined in `web.xml`
- Ensure required dependencies (Servlet API, GSON, MySQL driver) are available
- Use the compile script for building services
- Use ServicesDoc for documentation generation

---

*Built by Varnit Jain*