package iti.commons.configurer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Configurator {

    String path() default "";

    Property[] properties() default {};

    boolean systemProperties() default false;

    boolean environmentProperties() default false;

}