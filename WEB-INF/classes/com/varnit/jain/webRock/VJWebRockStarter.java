package com.varnit.jain.webRock;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.varnit.jain.webRock.annotations.Path;
import com.varnit.jain.webRock.annotations.GET;
import com.varnit.jain.webRock.annotations.POST;
import com.varnit.jain.webRock.annotations.FORWARD;
import com.varnit.jain.webRock.annotations.OnStartup;
import com.varnit.jain.webRock.model.webRockModel;
import com.varnit.jain.webRock.pojo.Service;

public class VJWebRockStarter extends HttpServlet {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("VJWebRock Framework: Starting Initialization...");
        
        ServletContext context = config.getServletContext();
        String packagePrefix = config.getInitParameter("SERVICE_PACKAGE_PREFIX");
        
        if (packagePrefix == null) {
            System.out.println("Warning: SERVICE_PACKAGE_PREFIX not found in web.xml. Scanning all classes.");
            packagePrefix = "";
        }

        String realPath = context.getRealPath("/WEB-INF/classes");
        if (realPath == null) {
            System.err.println("Error: Could not determine real path of /WEB-INF/classes");
            return;
        }

        File classesDir = new File(realPath);
        webRockModel model = new webRockModel();
        ArrayList<Service> startupList = new ArrayList<>();
        
        scanAndRegister(classesDir, classesDir, packagePrefix, model, startupList);
        
        // Phase 4: Handle Startup Lifecyle
        Collections.sort(startupList, (a, b) -> a.getPriority() - b.getPriority());
        for (Service s : startupList) {
            try {
                Object obj = s.getServiceClass().getDeclaredConstructor().newInstance();
                s.getService().invoke(obj);
            } catch (Exception e) {
                System.out.println("Error executing @OnStartup: " + e.getMessage());
            }
        }
        
        context.setAttribute("webRockModel", model);
        System.out.println("VJWebRock Framework: Initialization Complete. " + model.getMap().size() + " services mapped.");
    }

    private void scanAndRegister(File root, File current, String prefix, webRockModel model, ArrayList<Service> startupList) {
        File[] files = current.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanAndRegister(root, file, prefix, model, startupList);
            } else if (file.getName().endsWith(".class")) {
                String fullPath = file.getAbsolutePath();
                String rootPath = root.getAbsolutePath();
                
                // Extract relative path from root
                String relativePath = fullPath.substring(rootPath.length() + 1);
                
                // Convert path to class name (e.g., bobby\test\StudentService.class -> bobby.test.StudentService)
                String className = relativePath.replace(File.separatorChar, '.').replace(".class", "");
                
                if (className.startsWith(prefix)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(Path.class)) {
                            String classPath = clazz.getAnnotation(Path.class).value();
                            
                            boolean classGet = clazz.isAnnotationPresent(GET.class);
                            boolean classPost = clazz.isAnnotationPresent(POST.class);

                            Method[] methods = clazz.getDeclaredMethods();
                            for (Method method : methods) {
                                if (method.isAnnotationPresent(Path.class)) {
                                    String methodPath = method.getAnnotation(Path.class).value();
                                    String finalPath = classPath + methodPath;
                                    
                                    boolean methodGet = method.isAnnotationPresent(GET.class);
                                    boolean methodPost = method.isAnnotationPresent(POST.class);

                                    boolean isGetAllowed;
                                    boolean isPostAllowed;

                                    if (methodGet || methodPost) {
                                        isGetAllowed = methodGet;
                                        isPostAllowed = methodPost;
                                    } else if (classGet || classPost) {
                                        isGetAllowed = classGet;
                                        isPostAllowed = classPost;
                                    } else {
                                        isGetAllowed = true;
                                        isPostAllowed = true;
                                    }

                                    Service service = new Service();
                                    service.setServiceClass(clazz);
                                    service.setService(method);
                                    service.setPath(finalPath);
                                    service.setIsGetAllowed(isGetAllowed);
                                    service.setIsPostAllowed(isPostAllowed);
                                    
                                    if (method.isAnnotationPresent(FORWARD.class)) {
                                        service.setForwardTo(method.getAnnotation(FORWARD.class).value());
                                    }
                                    
                                    model.getMap().put(finalPath, service);
                                    System.out.println("VJWebRock: Mapped " + finalPath + " -> " + className + "." + method.getName() + " [GET=" + isGetAllowed + ", POST=" + isPostAllowed + "]");
                                }

                                if (method.isAnnotationPresent(OnStartup.class)) {
                                    if (method.getReturnType() != void.class || method.getParameterCount() != 0) {
                                        System.out.println("Invalid @OnStartup method: " + method.getName() + " in class " + clazz.getName());
                                        continue;
                                    }

                                    OnStartup os = method.getAnnotation(OnStartup.class);
                                    Service startupService = new Service();
                                    startupService.setServiceClass(clazz);
                                    startupService.setService(method);
                                    startupService.setRunOnStartup(true);
                                    startupService.setPriority(os.priority());
                                    startupList.add(startupService);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("VJWebRock: Failed to load class " + className);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
