package com.varnit.jain.webRock.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnStartup
{
    int priority() default 0;
}
