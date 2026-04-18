package com.varnit.jain.webRock.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecuredAccess {
    String checkPost();
    String guard();
}
