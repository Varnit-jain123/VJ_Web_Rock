package com.varnit.jain.webRock.pojo;

import java.lang.reflect.Method;

public class Service {
    private Class serviceClass;
    private String path;
    private Method service;
    private boolean isGetAllowed;
    private boolean isPostAllowed;
    private String forwardTo;
    private boolean runOnStartup;
    private int priority;

    public Service() {}

    public boolean getRunOnStartup() {
        return runOnStartup;
    }

    public void setRunOnStartup(boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public boolean isGetAllowed() {
        return isGetAllowed;
    }

    public void setIsGetAllowed(boolean isGetAllowed) {
        this.isGetAllowed = isGetAllowed;
    }

    public boolean isPostAllowed() {
        return isPostAllowed;
    }

    public void setIsPostAllowed(boolean isPostAllowed) {
        this.isPostAllowed = isPostAllowed;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getService() {
        return service;
    }

    public void setService(Method service) {
        this.service = service;
    }
}
