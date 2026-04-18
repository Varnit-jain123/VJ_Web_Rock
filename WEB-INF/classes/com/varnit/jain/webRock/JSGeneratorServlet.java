package com.varnit.jain.webRock;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.varnit.jain.webRock.annotations.Path;
import com.varnit.jain.webRock.annotations.POST;
import com.varnit.jain.webRock.model.webRockModel;
import com.varnit.jain.webRock.pojo.Service;

public class JSGeneratorServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("JSGenerator: Starting JS Proxy Generation...");
        
        ServletContext context = config.getServletContext();
        String jsFileName = context.getInitParameter("JsFile");
        if (jsFileName == null) jsFileName = "vjwebrock_proxy.js";

        String jsDirPath = context.getRealPath("/WEB-INF/js");
        File jsDir = new File(jsDirPath);
        if (!jsDir.exists()) jsDir.mkdirs();

        File jsFile = new File(jsDir, jsFileName);
        
        try (FileWriter fw = new FileWriter(jsFile)) {
            webRockModel model = (webRockModel) context.getAttribute("webRockModel");
            String packagePrefix = context.getInitParameter("SERVICE_PACKAGE_PREFIX");
            if (packagePrefix == null) packagePrefix = "";

            String classesPath = context.getRealPath("/WEB-INF/classes");
            File classesDir = new File(classesPath);

            StringBuilder jsCode = new StringBuilder();
            jsCode.append("// VJWebRock Auto-Generated Proxy SDK\n\n");

            // 1. Scan for POJOs and generate JS classes
            generatePOJOs(classesDir, classesDir, packagePrefix, jsCode);

            // 2. Generate Service Proxies from Model
            generateServiceProxies(model, jsCode);

            fw.write(jsCode.toString());
            System.out.println("JSGenerator: Successfully generated " + jsFileName + " at " + jsFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("JSGenerator: Error during generation");
            e.printStackTrace();
        }
    }

    private void generatePOJOs(File root, File current, String prefix, StringBuilder jsCode) {
        File[] files = current.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                generatePOJOs(root, file, prefix, jsCode);
            } else if (file.getName().endsWith(".class")) {
                String className = getClassName(root, file);
                if (className.startsWith(prefix)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        // POJOs are classes without @Path and aren't framework internal
                        if (!clazz.isAnnotationPresent(Path.class) && !className.contains("com.varnit.jain.webRock")) {
                            jsCode.append("class ").append(clazz.getSimpleName()).append(" {\n");
                            jsCode.append("  constructor() {\n");
                            for (Field field : clazz.getDeclaredFields()) {
                                jsCode.append("    this.").append(field.getName()).append(" = ").append(getDefaultValue(field.getType())).append(";\n");
                            }
                            jsCode.append("  }\n}\n\n");
                        }
                    } catch (Exception e) {}
                }
            }
        }
    }

    private void generateServiceProxies(webRockModel model, StringBuilder jsCode) {
        if (model == null) return;
        
        // Group services by class
        Map<Class<?>, List<Service>> grouped = new HashMap<>();
        for (Service s : model.getMap().values()) {
            grouped.computeIfAbsent(s.getServiceClass(), k -> new ArrayList<>()).add(s);
        }

        for (Map.Entry<Class<?>, List<Service>> entry : grouped.entrySet()) {
            Class<?> clazz = entry.getKey();
            jsCode.append("class ").append(clazz.getSimpleName()).append(" {\n");

            for (Service s : entry.getValue()) {
                Method m = s.getService();
                String path = s.getPath();
                boolean isPost = s.isPostAllowed() && !s.isGetAllowed(); // Heuristic: prefer POST if both
                // In our framework, if both allowed, we might default to GET. But if @POST present, use fetch POST.
                if (m.isAnnotationPresent(POST.class)) isPost = true;

                jsCode.append("  ").append(m.getName()).append("(data) {\n");
                jsCode.append("    let url = 'schoolService").append(path.startsWith("/") ? "" : "/").append(path).append("';\n");
                if (!isPost) {
                    jsCode.append("    if (data) {\n");
                    jsCode.append("      let params = new URLSearchParams(data).toString();\n");
                    jsCode.append("      if (params) url += '?' + params;\n");
                    jsCode.append("    }\n");
                }
                jsCode.append("    return fetch(url, {\n");
                jsCode.append("      method: '").append(isPost ? "POST" : "GET").append("'");
                if (isPost) {
                    jsCode.append(",\n      headers: {'Content-Type': 'application/json'},\n");
                    jsCode.append("      body: JSON.stringify(data)\n");
                } else {
                    jsCode.append("\n");
                }
                
                // Smart response handling based on return type
                if (m.getReturnType() == String.class) {
                    jsCode.append("    }).then(r => r.text());\n");
                } else {
                    jsCode.append("    }).then(r => r.json());\n");
                }
                jsCode.append("  }\n\n");
            }
            jsCode.append("}\n\n");
        }
    }

    private String getClassName(File root, File file) {
        String relative = file.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
        return relative.replace(File.separatorChar, '.').replace(".class", "");
    }

    private String getDefaultValue(Class<?> type) {
        if (type == int.class || type == long.class || type == float.class || type == double.class || type == byte.class || type == short.class) return "0";
        if (type == boolean.class) return "false";
        if (type == char.class) return "'\\u0000'";
        return "''";
    }
}
