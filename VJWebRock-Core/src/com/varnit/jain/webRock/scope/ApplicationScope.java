package com.varnit.jain.webRock.scope;

import javax.servlet.ServletContext;

public class ApplicationScope {
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setAttribute(String name, Object value) {
        servletContext.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return servletContext.getAttribute(name);
    }
}
