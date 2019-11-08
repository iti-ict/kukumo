/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.api.extensions;


import iti.commons.configurer.Configuration;
import iti.commons.jext.ExtensionPoint;

import java.util.function.Consumer;


@ExtensionPoint
public interface Configurator<T> {

    void configure(T contributor, Configuration configuration);


    boolean accepts(Object contributor);


    default <P> void ifPresent(
            Configuration configuration,
            String property,
            Class<P> propertyClass,
            Consumer<P> contributorConsumer
    ) {
        configuration.get(property,propertyClass).ifPresent(contributorConsumer);
    }

}
