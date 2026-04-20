# VJWebRock Framework 🚀

**VJWebRock** is a lightweight, high-performance Java Web Framework designed to simplify server-side development with a modern, annotation-driven approach. It implements proven architectural patterns like Front Controller, Dependency Injection, and Automated SDK generation, similar to Spring MVC and JAX-RS.

This repository is structured as a professional library with an integrated demo application.

---

## 📂 Repository Structure

The project is organized into the following main components:

- **[VJWebRock-Core](file:///c:/tomcat9/webapps/VJ_web_rock/VJWebRock-Core)**: The core framework library.
  - `src/`: Framework source code (Annotations, Scopes, JSON binding).
  - `make-jar.bat`: Automated script to build the official `vj-webrock.jar`.
- **Demo Application (Integrated)**:
  - **[src](file:///c:/tomcat9/webapps/VJ_web_rock/src)**: Example service implementations and business logic.
  - **[web](file:///c:/tomcat9/webapps/VJ_web_rock/web)**: Backup of web assets (HTML, JS, JSP) and `WEB-INF`.
  - **Root Directory**: Serves as the live deployment root for Tomcat.
- **[ServicesDoc](file:///c:/tomcat9/webapps/VJ_web_rock/ServicesDoc)**: A CLI tool to generate professional PDF documentation for your APIs.

---

## ✨ Key Features

### 1. Intelligent Routing
- **Annotation Based**: Use `@Path("/url")` on classes and methods to define clear, hierarchical routing.
- **HTTP Method Support**: Explicitly control access with `@GET` and `@POST` annotations.
- **Internal Forwarding**: Simple redirection using the `@FORWARD("/target")` annotation.

### 2. Powerful Dependency Injection
- **Scope Injection**: Inject `RequestScope`, `SessionScope`, and `ApplicationScope` directly into methods or class fields.
- **Auto-Wiring**: Use `@AutoWired(name="...")` to automatically pull objects from any scope into your service fields.
- **Parameter Injection**: Map request parameters seamlessly using `@RequestParameter` and `@InjectRequestParameter`.

### 3. Automated POJO & JSON Binding
- **Request Body Parsing**: Automatically converts incoming JSON request bodies into Java POJO objects using GSON.
- **Response Serialization**: Automatically serializes Java return types into formatted JSON responses.

### 4. JavaScript Proxy SDK
- **Zero-Config Client**: The framework automatically generates a JavaScript SDK (`vj-webrock.js`) mirroring your server-side services.
- **Easy Integration**: Call server-side methods as if they were local JavaScript functions with full `fetch` and `Promise` support.

### 5. Security Layer & Lifecycle
- **Guards**: Secure classes with `@SecuredAccess`, specifying a `Guard` class to validate requests.
- **Startup Hooks**: Use `@OnStartup` to execute code as soon as the framework initializes.

---

## 🚀 Getting Started & Execution

### 1. Build the Framework Library
Navigate to the `VJWebRock-Core` folder and run **`make-jar.bat`**. This creates `vj-webrock.jar` and automatically syncs it to the demo projects.

### 2. Compile the Demo Application
Run the **`compile`** script from the project root. This will compile all demo services into `WEB-INF/classes`.

### 3. Run the Application (Tomcat 9)
Point your Tomcat deployment to the root of this repository.
- **URL**: `http://localhost:8080/VJ_web_rock/CalculatorDemo.html`
- **SDK Access**: Visit `/js/vj-webrock.js` to see your auto-generated Proxy SDK.

---

## 🛡️ The "Bobby" Validation Rules

To ensure framework stability, the following rules must be strictly applied to all services:

1.  **Public Access**: All service classes and mapped methods must be `public`.
2.  **OnStartup Integrity**: `@OnStartup` methods must be `void` and have **exactly zero** parameters.
3.  **Setter Requirement**: Any field using `@AutoWired` or `@InjectRequestParameter` **MUST** have a corresponding public setter method.
4.  **Unique Paths**: Multiple services cannot map to the same URL path.
5.  **Single JSON Body**: A service method can have at most **one** parameter treated as the JSON request body.
6.  **Path Formatting**: All `@Path` values should follow a consistent slash-prefixing convention (e.g., `@Path("/student")`).

---

## 📈 The Evolution of VJWebRock

| Phase | Milestone | Description |
| :--- | :--- | :--- |
| **Initial** | **Core Foundation** | Established Reflection-based annotation processing and basic class loading. |
| **2nd** | **Basic Routing** | Implemented the Front Controller and mapped paths to Controller classes. |
| **4th** | **Lifecycle Hooks** | Introduced `@OnStartup` for prioritized initialization logic. |
| **6th** | **Auto-Wiring** | Built the Dependency Injection engine using the `@AutoWired` annotation. |
| **10th** | **JSON Inbound** | Integrated GSON for automated request body to POJO conversion. |
| **13th** | **Proxy SDK Gen** | **Zero-Config JavaScript Proxy SDK Generation for client-side integration.** |

---

## 🛠️ Tooling & Database

### Documentation Tool: ServicesDoc
A standalone CLI tool to analyze your compiled application, perform validation checks, and generate professional PDF documentation.

### Database Integration (MySQL)
VJWebRock simplifies database interaction. Simply add `mysql-connector-java.jar` to your `WEB-INF/lib` folder and use standard JDBC inside your service methods.

---

*Built by Varnit Jain*
