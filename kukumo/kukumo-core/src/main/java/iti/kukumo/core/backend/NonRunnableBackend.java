/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.core.backend;


import java.util.List;

import iti.commons.configurer.Configuration;
import iti.kukumo.api.KukumoDataTypeRegistry;
import iti.kukumo.api.plan.PlanNode;

/*
 * This implementation of Backend does not allow to run tests. Its main purpose
 * is just offer information about available steps to third-party components
 * (like completion tools)
 */
public class NonRunnableBackend extends AbstractBackend {


    public NonRunnableBackend(
        Configuration configuration,
        KukumoDataTypeRegistry typeRegistry,
        List<RunnableStep> steps
    ) {
        super(configuration,typeRegistry,steps);
    }



    @Override
    public void runStep(PlanNode step) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setUp() {
        // nothing
    }


    @Override
    public void tearDown() {
        // nothing
    }


}
