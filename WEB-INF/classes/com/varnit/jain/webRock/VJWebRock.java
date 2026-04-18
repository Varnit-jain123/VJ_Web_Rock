package com.varnit.jain.webRock;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.varnit.jain.webRock.model.webRockModel;
import com.varnit.jain.webRock.pojo.Service;
import com.varnit.jain.webRock.scope.*;
import com.varnit.jain.webRock.annotations.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

public class VJWebRock extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, "POST");
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, String methodType) throws ServletException, IOException {
        // For url-pattern /schoolService/*, pathInfo will contain the part after /schoolService
        // Example: /schoolService/student/add -> pathInfo is /student/add
        String pathInfo = request.getPathInfo();
        
        System.out.println("VJWebRock: Intercepted request for path: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service path not specified");
            return;
        }

        webRockModel model = (webRockModel) getServletContext().getAttribute("webRockModel");
        
        if (model != null && model.getMap().containsKey(pathInfo)) {
            Service service = model.getMap().get(pathInfo);
            
            if (methodType.equals("GET") && !service.isGetAllowed()) {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HTTP GET Method Not Allowed");
                return;
            }
            if (methodType.equals("POST") && !service.isPostAllowed()) {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HTTP POST Method Not Allowed");
                return;
            }

            try {
                // Create instance of the service class
                Object controller = service.getServiceClass().getDeclaredConstructor().newInstance();
                Class<?> clazz = service.getServiceClass();

                // Phase 5: Scope Injection
                if (clazz.isAnnotationPresent(InjectApplicationScope.class)) {
                    try {
                        Method m = clazz.getMethod("setApplicationScope", ApplicationScope.class);
                        ApplicationScope as = new ApplicationScope();
                        as.setServletContext(getServletContext());
                        m.invoke(controller, as);
                    } catch (Exception e) {
                        System.out.println("ApplicationScope setter missing in " + clazz.getName());
                    }
                }

                if (clazz.isAnnotationPresent(InjectSessionScope.class)) {
                    try {
                        Method m = clazz.getMethod("setSessionScope", SessionScope.class);
                        SessionScope ss = new SessionScope();
                        ss.setSession(request.getSession());
                        m.invoke(controller, ss);
                    } catch (Exception e) {
                        System.out.println("SessionScope setter missing in " + clazz.getName());
                    }
                }

                if (clazz.isAnnotationPresent(InjectRequestScope.class)) {
                    try {
                        Method m = clazz.getMethod("setRequestScope", RequestScope.class);
                        RequestScope rs = new RequestScope();
                        rs.setRequest(request);
                        m.invoke(controller, rs);
                    } catch (Exception e) {
                        System.out.println("RequestScope setter missing in " + clazz.getName());
                    }
                }

                if (clazz.isAnnotationPresent(InjectApplicationDirectory.class)) {
                    try {
                        Method m = clazz.getMethod("setApplicationDirectory", ApplicationDirectory.class);
                        String realPath = getServletContext().getRealPath("/");
                        ApplicationDirectory ad = new ApplicationDirectory(realPath);
                        m.invoke(controller, ad);
                    } catch (Exception e) {
                        System.out.println("ApplicationDirectory setter missing in " + clazz.getName());
                    }
                }

                // Phase 6: Named @AutoWired Field Injection
                List<Field> autoFields = service.getAutoWiredFields();
                if (autoFields != null) {
                    for (Field field : autoFields) {
                        try {
                            field.setAccessible(true);
                            AutoWired aw = field.getAnnotation(AutoWired.class);
                            String name = aw.name();
                            
                            Object value = request.getAttribute(name);
                            if (value == null) {
                                javax.servlet.http.HttpSession session = request.getSession(false);
                                if (session != null) value = session.getAttribute(name);
                            }
                            if (value == null) {
                                value = getServletContext().getAttribute(name);
                            }

                            if (value != null) {
                                if (field.getType().isAssignableFrom(value.getClass())) {
                                    field.set(controller, value);
                                } else {
                                    System.out.println("Type mismatch for field: " + field.getName() + " in " + clazz.getName() + ". Expected " + field.getType().getName() + " but found " + value.getClass().getName());
                                }
                            } else {
                                field.set(controller, null);
                            }
                        } catch (Exception e) {
                            System.out.println("Error in @AutoWired injection for field " + field.getName() + ": " + e.getMessage());
                        }
                    }
                }
                
                // Invoke the mapped method
                Object result = service.getService().invoke(controller);
                
                if (service.getForwardTo() != null) {
                    String forwardPath = service.getForwardTo();
                    Service targetService = model.getMap().get(forwardPath);
                    
                    if (targetService != null) {
                        // Internal forward
                        Object targetController = targetService.getServiceClass().getDeclaredConstructor().newInstance();
                        Object targetResult = targetService.getService().invoke(targetController);
                        
                        response.setContentType("text/html");
                        response.getWriter().print(targetResult != null ? targetResult.toString() : "");
                    } else {
                        // Resource forward (JSP/HTML)
                        javax.servlet.RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
                        if (rd != null) {
                            rd.forward(request, response);
                            return;
                        } else {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Forward target not found: " + forwardPath);
                            return;
                        }
                    }
                } else {
                    // Normal response
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.print(result != null ? result.toString() : "");
                }
                
            } catch (Exception e) {
                System.err.println("VJWebRock: Error invoking service method");
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Logic failure: " + e.getMessage());
            }
        } else {
            System.out.println("VJWebRock: No mapping found for " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found");
        }
    }
}
