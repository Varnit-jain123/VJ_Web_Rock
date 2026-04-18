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
import com.google.gson.Gson;
import java.io.BufferedReader;

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

                // Phase 11: Security Layer (@SecuredAccess)
                if (clazz.isAnnotationPresent(SecuredAccess.class)) {
                    SecuredAccess sa = clazz.getAnnotation(SecuredAccess.class);
                    String secClassName = sa.checkPost();
                    String guardMethodName = sa.guard();

                    try {
                        Class<?> secClass = Class.forName(secClassName);
                        Object secObj = secClass.getDeclaredConstructor().newInstance();

                        // Perform Scope Injection for Security Class
                        injectScopes(secObj, secClass, request);

                        // Invoke Guard Method (no parameters)
                        Method guardMethod = secClass.getMethod(guardMethodName);
                        guardMethod.invoke(secObj);
                    } catch (Exception e) {
                        System.out.println("Security check failed: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unauthorized Access (Security Guard rejected)");
                        return;
                    }
                }

                // Phase 5: Scope Injection (Refactored to use helper)
                injectScopes(controller, clazz, request);

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

                // Phase 8: Class-level @InjectRequestParameter injection
                List<Field> injectFields = service.getInjectRequestParameterFields();
                if (injectFields != null) {
                    for (Field field : injectFields) {
                        try {
                            InjectRequestParameter irp = field.getAnnotation(InjectRequestParameter.class);
                            String paramName = irp.value();
                            String value = request.getParameter(paramName);
                            
                            Class<?> type = field.getType();
                            Object convertedValue = null;

                            if (value != null) {
                                try {
                                    if (type == int.class || type == Integer.class)
                                        convertedValue = Integer.parseInt(value);
                                    else if (type == double.class || type == Double.class)
                                        convertedValue = Double.parseDouble(value);
                                    else if (type == float.class || type == Float.class)
                                        convertedValue = Float.parseFloat(value);
                                    else if (type == long.class || type == Long.class)
                                        convertedValue = Long.parseLong(value);
                                    else if (type == boolean.class || type == Boolean.class)
                                        convertedValue = Boolean.parseBoolean(value);
                                    else if (type == char.class || type == Character.class) {
                                        if (value.length() > 0)
                                            convertedValue = value.charAt(0);
                                        else
                                            System.out.println("Empty char for: " + paramName);
                                    } else if (type == String.class)
                                        convertedValue = value;
                                    else
                                        System.out.println("Unsupported type: " + type.getName());
                                } catch (Exception e) {
                                    System.out.println("Conversion error for: " + paramName + " in class " + clazz.getName());
                                }
                            }

                            // Construct setter name
                            String fieldName = field.getName();
                            String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                            try {
                                Method setter = clazz.getMethod(setterName, type);
                                setter.invoke(controller, convertedValue);
                            } catch (NoSuchMethodException e) {
                                System.out.println("Setter not found for field: " + fieldName + " in " + clazz.getName());
                            }
                        } catch (Exception e) {
                            System.out.println("Error in @InjectRequestParameter injection: " + e.getMessage());
                        }
                    }
                }

                // Phase 10: JSON Request Body Binding using GSON (Unified Logic)
                Method method = service.getService();
                java.lang.reflect.Parameter[] parameters = method.getParameters();
                int n = parameters.length;
                Object[] args = new Object[n];

                if (n > 0) {
                    Gson gson = new Gson();
                    boolean bodyRead = false;
                    String jsonBody = "";
                    boolean jsonUsed = false;

                    for (int i = 0; i < n; i++) {
                        java.lang.reflect.Parameter param = parameters[i];
                        Class<?> type = param.getType();

                        // 1. Check for Framework Scopes
                        if (type == SessionScope.class) {
                            SessionScope ss = new SessionScope();
                            ss.setSession(request.getSession());
                            args[i] = ss;
                        } else if (type == ApplicationScope.class) {
                            ApplicationScope as = new ApplicationScope();
                            as.setServletContext(getServletContext());
                            args[i] = as;
                        } else if (type == RequestScope.class) {
                            RequestScope rs = new RequestScope();
                            rs.setRequest(request);
                            args[i] = rs;
                        } else if (type == ApplicationDirectory.class) {
                            String path = getServletContext().getRealPath("/");
                            args[i] = new ApplicationDirectory(path);
                        } 
                        // 2. Check for @RequestParameter (Phase 7 support)
                        else if (param.isAnnotationPresent(RequestParameter.class)) {
                            RequestParameter rp = param.getAnnotation(RequestParameter.class);
                            String paramName = rp.value();
                            String value = request.getParameter(paramName);
                            Object convertedValue = null;

                            if (value != null) {
                                try {
                                    if (type == int.class || type == Integer.class)
                                        convertedValue = Integer.parseInt(value);
                                    else if (type == double.class || type == Double.class)
                                        convertedValue = Double.parseDouble(value);
                                    else if (type == float.class || type == Float.class)
                                        convertedValue = Float.parseFloat(value);
                                    else if (type == long.class || type == Long.class)
                                        convertedValue = Long.parseLong(value);
                                    else if (type == boolean.class || type == Boolean.class)
                                        convertedValue = Boolean.parseBoolean(value);
                                    else if (type == char.class || type == Character.class) {
                                        if (value.length() > 0) convertedValue = value.charAt(0);
                                    } else if (type == String.class)
                                        convertedValue = value;
                                } catch (Exception e) {
                                    System.out.println("Conversion error for: " + paramName);
                                }
                            }
                            args[i] = convertedValue;
                        }
                        // 3. Otherwise, treat as JSON Body (Phase 10)
                        else {
                            if (jsonUsed) {
                                response.sendError(500, "Multiple JSON parameters not allowed");
                                return;
                            }
                            
                            if (!bodyRead) {
                                try {
                                    BufferedReader br = request.getReader();
                                    StringBuilder sb = new StringBuilder();
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        sb.append(line);
                                    }
                                    jsonBody = sb.toString();
                                    bodyRead = true;
                                } catch (Exception e) {
                                    System.out.println("Error reading request body: " + e.getMessage());
                                }
                            }
                            
                            try {
                                args[i] = gson.fromJson(jsonBody, type);
                                jsonUsed = true;
                            } catch (Exception e) {
                                System.out.println("JSON Parsing Error: " + e.getMessage());
                                response.sendError(500, "JSON Parsing Error");
                                return;
                            }
                        }
                    }
                }
                
                // Invoke the mapped method with arguments
                Object result = method.invoke(controller, args);
                
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
                    if (result == null) {
                        response.setContentType("text/html");
                        response.getWriter().print("");
                    } else if (result instanceof String) {
                        response.setContentType("text/html");
                        response.getWriter().print(result.toString());
                    } else {
                        // Phase 13: JSON Response Serialization
                        response.setContentType("application/json");
                        Gson gson = new Gson();
                        String jsonResponse = gson.toJson(result);
                        response.getWriter().print(jsonResponse);
                    }
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
    
    private void injectScopes(Object target, Class<?> clazz, HttpServletRequest request) {
        if (clazz.isAnnotationPresent(InjectApplicationScope.class)) {
            try {
                Method m = clazz.getMethod("setApplicationScope", ApplicationScope.class);
                ApplicationScope as = new ApplicationScope();
                as.setServletContext(getServletContext());
                m.invoke(target, as);
            } catch (Exception e) {
                System.out.println("ApplicationScope setter missing in " + clazz.getName());
            }
        }

        if (clazz.isAnnotationPresent(InjectSessionScope.class)) {
            try {
                Method m = clazz.getMethod("setSessionScope", SessionScope.class);
                SessionScope ss = new SessionScope();
                ss.setSession(request.getSession());
                m.invoke(target, ss);
            } catch (Exception e) {
                System.out.println("SessionScope setter missing in " + clazz.getName());
            }
        }

        if (clazz.isAnnotationPresent(InjectRequestScope.class)) {
            try {
                Method m = clazz.getMethod("setRequestScope", RequestScope.class);
                RequestScope rs = new RequestScope();
                rs.setRequest(request);
                m.invoke(target, rs);
            } catch (Exception e) {
                System.out.println("RequestScope setter missing in " + clazz.getName());
            }
        }

        if (clazz.isAnnotationPresent(InjectApplicationDirectory.class)) {
            try {
                Method m = clazz.getMethod("setApplicationDirectory", ApplicationDirectory.class);
                String realPath = getServletContext().getRealPath("/");
                ApplicationDirectory ad = new ApplicationDirectory(realPath);
                m.invoke(target, ad);
            } catch (Exception e) {
                System.out.println("ApplicationDirectory setter missing in " + clazz.getName());
            }
        }
    }
}
