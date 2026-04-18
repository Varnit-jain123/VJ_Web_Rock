# ServicesDoc 📄

**ServicesDoc** is a standalone CLI tool designed to generate clean, professional PDF documentation for applications built using the **VJWebRock** framework. It analyzes compiled Java classes to map out service endpoints, dependency injections, and security configurations.

---

##  Objective
To provide developers with an offline analyzer that automatically documents:
*   **Service Endpoints**: URL mappings, class/method names, parameters, and return types.
*   **Injection Mapping**: Class-level scopes and field-level `@AutoWired` dependencies.
*   **Security Visibility**: Guard classes and methods for secured access.

---

##  Project Structure
```text
ServicesDoc/
├── src/            # Source code (com.varnit.jain.webRock.tools)
├── lib/            # External dependencies (iText 7.1.11 Jars)
├── build/          # Temporary compilation directory
├── dist/           # Final distribution (JAR)
│   ├── ServicesDoc.jar


```

---

## ⚙️ Prerequisites
*   **Java JDK 8+**
*   **iText 7 Jars** (placed in the `lib/` directory)
*   **VJWebRock Framework classes** (available in your classpath)

---

## 🏃 How to Run
You can run the generated JAR using the follow command:

```powershell
java --classpath "dist/ServicesDoc.jar;lib/*;C:/tomcat9/webapps/VJ_web_rock/WEB-INF/classes" com.varnit.jain.webRock.tools.ServicesDoc "path/to/WEB-INF/classes" "output.pdf (path to store pdf along with pdf name"

``` EXAMPLE : ```
java -classpath "dist/ServicesDoc.jar;lib/*;C:/tomcat9/webapps/VJ_web_rock/WEB-INF/classes" com.varnit.jain.webRock.tools.ServicesDoc "C:/tomcat9/webapps/VJ_web_rock/WEB-INF/classes" "C:/programming/output.pdf"

```

### 🔹 Example Output
The PDF will contain structured tables for:
1.  **Service Endpoints**: URL, Class, Method, Parameters, Return Type.
2.  **Injections & Scopes**: Class name and injected types.
3.  **Security Mapping**: Class name, Guard Class, and Guard Method.

---

## 🛠️ Technologies Used
*   **Language**: Java
*   **PDF Library**: [iText 7](https://itextpdf.com/products/itext-7)
*   **Extraction**: Java Reflection API & Annotations

---

## 📝 Note
This tool is a **standalone analyzer** and does not require a running Tomcat instance. It scans the compiled `.class` files directly.
