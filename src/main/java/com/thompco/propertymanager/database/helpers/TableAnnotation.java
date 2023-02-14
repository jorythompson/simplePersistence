package com.thompco.propertymanager.database.helpers;


import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAnnotation {
    Class<?> type();

    boolean allowNull() default false;
}
