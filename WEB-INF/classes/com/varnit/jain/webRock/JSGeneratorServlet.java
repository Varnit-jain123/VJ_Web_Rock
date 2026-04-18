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
        
        String jsDirPath = context.getRealPath("/WEB-INF/js");
        File jsDir = new File(jsDirPath);
        if (!jsDir.exists()) jsDir.mkdirs();

        webRockModel model = (webRockModel) context.getAttribute("webRockModel");
        String packagePrefix = context.getInitParameter("SERVICE_PACKAGE_PREFIX");
        if (packagePrefix == null) packagePrefix = "";

        String classesPath = context.getRealPath("/WEB-INF/classes");
        File classesDir = new File(classesPath);

        // Map ClassName -> JS Content
        Map<String, String> pojoMap = new HashMap<>();
        Map<String, String> proxyMap = new HashMap<>();

        collectPOJOs(classesDir, classesDir, packagePrefix, pojoMap);
        collectProxies(model, proxyMap);

        if (jsFileName != null && !jsFileName.trim().isEmpty()) {
            // Case A: Aggregate Mode
            System.out.println("JSGenerator: Aggregate mode enabled. Creating " + jsFileName);
            File jsFile = new File(jsDir, jsFileName);
            try (FileWriter fw = new FileWriter(jsFile)) {
                fw.write("// VJWebRock Auto-Generated Proxy SDK (Aggregate)\n\n");
                for (String code : pojoMap.values()) fw.write(code);
                for (String code : proxyMap.values()) fw.write(code);
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            // Case B: Modular Mode
            System.out.println("JSGenerator: Modular mode enabled. Creating individual files.");
            for (Map.Entry<String, String> entry : pojoMap.entrySet()) {
                writeIndividualFile(jsDir, entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : proxyMap.entrySet()) {
                writeIndividualFile(jsDir, entry.getKey(), entry.getValue());
            }
        }
    }

    private void writeIndividualFile(File dir, String className, String content) {
        File file = new File(dir, className + ".js");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("// VJWebRock Auto-Generated Proxy (" + className + ")\n\n");
            fw.write(content);
        } catch (IOException e) {
            System.err.println("Error writing " + file.getName());
            e.printStackTrace();
        }
    }

    private void collectPOJOs(File root, File current, String prefix, Map<String, String> map) {
        File[] files = current.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                collectPOJOs(root, file, prefix, map);
            } else if (file.getName().endsWith(".class")) {
                String className = getClassName(root, file);
                if (className.startsWith(prefix)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (!clazz.isAnnotationPresent(Path.class) && !className.contains("com.varnit.jain.webRock")) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("class ").append(clazz.getSimpleName()).append(" {\n");
                            sb.append("  constructor() {\n");
                            for (Field field : clazz.getDeclaredFields()) {
                                sb.append("    this.").append(field.getName()).append(" = ").append(getDefaultValue(field.getType())).append(";\n");
                            }
                            sb.append("  }\n}\n\n");
                            map.put(clazz.getSimpleName(), sb.toString());
                        }
                    } catch (Exception e) {}
                }
            }
        }
    }

    private void collectProxies(webRockModel model, Map<String, String> map) {
        if (model == null) return;
        Map<Class<?>, List<Service>> grouped = new HashMap<>();
        for (Service s : model.getMap().values()) {
            grouped.computeIfAbsent(s.getServiceClass(), k -> new ArrayList<>()).add(s);
        }

        for (Map.Entry<Class<?>, List<Service>> entry : grouped.entrySet()) {
            Class<?> clazz = entry.getKey();
            StringBuilder sb = new StringBuilder();
            sb.append("class ").append(clazz.getSimpleName()).append(" {\n");

            for (Service s : entry.getValue()) {
                Method m = s.getService();
                String path = s.getPath();
                boolean isPost = s.isPostAllowed() && !s.isGetAllowed();
                if (m.isAnnotationPresent(POST.class)) isPost = true;

                sb.append("  ").append(m.getName()).append("(data) {\n");
                sb.append("    let url = 'schoolService").append(path.startsWith("/") ? "" : "/").append(path).append("';\n");
                if (!isPost) {
                    sb.append("    if (data) {\n");
                    sb.append("      let params = new URLSearchParams(data).toString();\n");
                    sb.append("      if (params) url += '?' + params;\n");
                    sb.append("    }\n");
                }
                sb.append("    return fetch(url, {\n");
                sb.append("      method: '").append(isPost ? "POST" : "GET").append("'");
                if (isPost) {
                    sb.append(",\n      headers: {'Content-Type': 'application/json'},\n");
                    sb.append("      body: JSON.stringify(data)\n");
                } else {
                    sb.append("\n");
                }
                
                if (m.getReturnType() == String.class) {
                    sb.append("    }).then(r => r.text());\n");
                } else {
                    sb.append("    }).then(r => r.json());\n");
                }
                sb.append("  }\n\n");
            }
            sb.append("}\n\n");
            map.put(clazz.getSimpleName(), sb.toString());
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
