package com.varnit.jain.webRock.pojo;

import java.lang.reflect.Method;

public class Service {
    private Class serviceClass;
    private String path;
    private Method service;
    private boolean isGetAllowed;
    private boolean isPostAllowed;

    public Service() {}

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
