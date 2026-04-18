package com.varnit.jain.webRock.tools;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import com.varnit.jain.webRock.annotations.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;

public class ServicesDoc {

    private static java.util.List<ServiceInfo> services = new java.util.ArrayList<>();
    private static java.util.List<InjectionInfo> injections = new java.util.ArrayList<>();
    private static java.util.List<SecurityInfo> securityList = new java.util.ArrayList<>();
    private static java.util.List<String> errors = new java.util.ArrayList<>();
    private static Set<String> urls = new HashSet<>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    "Usage: java com.varnit.jain.webRock.tools.ServicesDoc <path-to-WEB-INF/classes> <output-pdf-path>");
            return;
        }

        String classesPath = args[0];
        String outputPath = args[1];

        try {
            File root = new File(classesPath);
            if (!root.exists() || !root.isDirectory()) {
                System.err.println("Error: Provided path is not a valid directory.");
                return;
            }

            // Setup ClassLoader to include the scanned directory
            URL[] urlsArr = { root.toURI().toURL() };
            URLClassLoader classLoader = new URLClassLoader(urlsArr, ServicesDoc.class.getClassLoader());

            // Scan and Process
            scanAndProcess(root, "", classLoader);

            // Generate PDF
            generatePDF(outputPath);

            System.out.println("Documentation generated successfully at: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void scanAndProcess(File dir, String packageName, URLClassLoader loader) {
        File[] files = dir.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanAndProcess(file, subPackage, loader);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = loader.loadClass(className);
                    processClass(clazz);
                } catch (Throwable t) {
                    errors.add("Failed to load class: " + className + " (" + t.getMessage() + ")");
                }
            }
        }
    }

    private static void processClass(Class<?> clazz) {
        // 1. Detect Service
        if (clazz.isAnnotationPresent(Path.class)) {
            String basePath = clazz.getAnnotation(Path.class).value();

            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Path.class)) {
                    String fullPath = basePath + m.getAnnotation(Path.class).value();

                    // Duplicate URL check
                    if (urls.contains(fullPath)) {
                        errors.add(
                                "Duplicate URL detected: " + fullPath + " in " + clazz.getName() + "." + m.getName());
                    } else {
                        urls.add(fullPath);
                    }

                    ServiceInfo s = new ServiceInfo();
                    s.url = fullPath;
                    s.className = clazz.getName();
                    s.methodName = m.getName();
                    s.returnType = m.getReturnType().getSimpleName();

                    StringBuilder paramStr = new StringBuilder();
                    for (Parameter p : m.getParameters()) {
                        paramStr.append(p.getType().getSimpleName()).append(", ");
                    }
                    s.params = paramStr.length() > 0 ? paramStr.substring(0, paramStr.length() - 2) : "";
                    services.add(s);
                }

                // @OnStartup Validation
                if (m.isAnnotationPresent(OnStartup.class)) {
                    validateOnStartup(clazz, m);
                }
            }
        }

        // 2. Detect Injection
        java.util.List<String> injects = new java.util.ArrayList<>();
        if (clazz.isAnnotationPresent(InjectSessionScope.class))
            injects.add("SessionScope");
        if (clazz.isAnnotationPresent(InjectRequestScope.class))
            injects.add("RequestScope");
        if (clazz.isAnnotationPresent(InjectApplicationScope.class))
            injects.add("ApplicationScope");
        if (clazz.isAnnotationPresent(InjectApplicationDirectory.class))
            injects.add("ApplicationDirectory");

        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(AutoWired.class)) {
                injects.add("AutoWired: " + f.getName());
                validateAutoWired(clazz, f);
            }
        }

        if (!injects.isEmpty()) {
            InjectionInfo i = new InjectionInfo();
            i.className = clazz.getName();
            i.types = String.join(", ", injects);
            injections.add(i);
        }

        // 3. Detect Security
        if (clazz.isAnnotationPresent(SecuredAccess.class)) {
            SecuredAccess sa = clazz.getAnnotation(SecuredAccess.class);
            SecurityInfo si = new SecurityInfo();
            si.className = clazz.getName();
            si.guardClass = sa.checkPost();
            si.guardMethod = sa.guard();
            securityList.add(si);
        }
    }

    private static void validateOnStartup(Class<?> clazz, Method m) {
        if (!Modifier.isPublic(m.getModifiers())) {
            errors.add("Invalid @OnStartup: " + clazz.getName() + "." + m.getName() + " must be public.");
        }
        if (m.getParameterCount() > 0) {
            errors.add("Invalid @OnStartup: " + clazz.getName() + "." + m.getName() + " should not have parameters.");
        }
    }

    private static void validateAutoWired(Class<?> clazz, Field f) {
        String setterName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
        try {
            clazz.getMethod(setterName, f.getType());
        } catch (NoSuchMethodException e) {
            errors.add("Missing setter: " + clazz.getName() + " needs " + setterName + "(" + f.getType().getSimpleName()
                    + ")");
        }
    }

    private static void generatePDF(String outputPath) throws IOException {
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        document.add(new Paragraph("VJWebRock Service Documentation")
                .setFontSize(22)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
        document.add(new Paragraph("Generated on: " + new Date().toString())
                .setFontSize(10)
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        // 1. Service Table
        if (!services.isEmpty()) {
            document.add(new Paragraph("1. Service Endpoints").setBold().setFontSize(16));
            Table serviceTable = new Table(UnitValue.createPercentArray(new float[] { 30, 25, 15, 20, 10 }))
                    .useAllAvailableWidth();
            serviceTable.addHeaderCell("URL");
            serviceTable.addHeaderCell("Class");
            serviceTable.addHeaderCell("Method");
            serviceTable.addHeaderCell("Parameters");
            serviceTable.addHeaderCell("Return Type");

            for (ServiceInfo s : services) {
                serviceTable.addCell(s.url);
                serviceTable.addCell(s.className);
                serviceTable.addCell(s.methodName);
                serviceTable.addCell(s.params);
                serviceTable.addCell(s.returnType);
            }
            document.add(serviceTable);
            document.add(new Paragraph("\n"));
        }

        // 2. Injection Table
        if (!injections.isEmpty()) {
            document.add(new Paragraph("2. Injections & Scopes").setBold().setFontSize(16));
            Table injectionTable = new Table(UnitValue.createPercentArray(new float[] { 40, 60 }))
                    .useAllAvailableWidth();
            injectionTable.addHeaderCell("Class");
            injectionTable.addHeaderCell("Injectables");

            for (InjectionInfo i : injections) {
                injectionTable.addCell(i.className);
                injectionTable.addCell(i.types);
            }
            document.add(injectionTable);
            document.add(new Paragraph("\n"));
        }

        // 3. Security Section
        if (!securityList.isEmpty()) {
            document.add(new Paragraph("3. Security Mapping").setBold().setFontSize(16));
            Table securityTable = new Table(UnitValue.createPercentArray(new float[] { 40, 30, 30 }))
                    .useAllAvailableWidth();
            securityTable.addHeaderCell("Class");
            securityTable.addHeaderCell("Guard Class");
            securityTable.addHeaderCell("Guard Method");

            for (SecurityInfo s : securityList) {
                securityTable.addCell(s.className);
                securityTable.addCell(s.guardClass);
                securityTable.addCell(s.guardMethod);
            }
            document.add(securityTable);
            document.add(new Paragraph("\n"));
        }

        document.close();
    }

    // Supporting classes
    static class ServiceInfo {
        String url;
        String className;
        String methodName;
        String params;
        String returnType;
    }

    static class InjectionInfo {
        String className;
        String types;
    }

    static class SecurityInfo {
        String className;
        String guardClass;
        String guardMethod;
    }
}
