package com.varnit.jain.webRock.scope;

import javax.servlet.http.HttpServletRequest;

public class RequestScope {
    private HttpServletRequest request;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }
}
