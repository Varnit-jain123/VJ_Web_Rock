package com.varnit.jain.webRock.scope;

import javax.servlet.http.HttpSession;

public class SessionScope {
    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }
}
