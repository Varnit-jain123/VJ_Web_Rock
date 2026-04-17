package com.varnit.jain.webRock;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.varnit.jain.webRock.model.webRockModel;
import com.varnit.jain.webRock.pojo.Service;

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
