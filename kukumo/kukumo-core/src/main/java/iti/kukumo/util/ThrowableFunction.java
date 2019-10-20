/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.util;


import java.util.function.Function;

import iti.kukumo.api.KukumoException;


@FunctionalInterface
public interface ThrowableFunction<T, R> extends java.util.function.Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return applyThrowable(t);
        } catch (Exception e) {
            throw new KukumoException(e);
        }
    }


    R applyThrowable(T t) throws Exception;


    default <U> ThrowableFunction<T, U> andThen(ThrowableFunction<R, U> chainFunction) {
        return t -> chainFunction.apply(this.apply(t));
    }


    static <T, R> Function<T, R> unchecked(ThrowableFunction<T, R> throwableFunction) {
        return throwableFunction::apply;
    }

}
