/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.database.test.dialect;


import org.junit.runner.RunWith;

import iti.commons.configurer.AnnotatedConfiguration;
import iti.commons.configurer.Property;
import iti.kukumo.api.KukumoConfiguration;
import iti.kukumo.database.DatabaseConfigContributor;
import iti.kukumo.junit.KukumoJUnitRunner;


@RunWith(KukumoJUnitRunner.class)
@AnnotatedConfiguration(properties = {
    @Property(key = KukumoConfiguration.RESOURCE_TYPES, value = "gherkin"),
    @Property(key = KukumoConfiguration.RESOURCE_PATH, value = "src/test/resources/features"),
    @Property(key = DatabaseConfigContributor.DATABASE_CONNECTION_URL, value = "jdbc:h2:mem:test;INIT=runscript from 'src/test/resources/create-schema.sql'"),
    @Property(key = DatabaseConfigContributor.DATABASE_CONNECTION_USERNAME, value = "sa"),
    @Property(key = DatabaseConfigContributor.DATABASE_CONNECTION_PASSWORD, value = ""),
    @Property(key = DatabaseConfigContributor.DATABASE_METADATA_CASE_SENSITIVITY, value = "upper_cased")
})
public class H2DbLoader {


}
