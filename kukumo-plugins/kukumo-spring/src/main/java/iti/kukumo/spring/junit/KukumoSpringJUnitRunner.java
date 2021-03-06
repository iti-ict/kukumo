/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.spring.junit;


import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.springframework.test.context.TestContextManager;

import iti.kukumo.core.Kukumo;
import iti.kukumo.core.junit.KukumoJUnitRunner;


public class KukumoSpringJUnitRunner extends Runner {

    private static final Logger LOGGER = Kukumo.LOGGER;
    private final KukumoJUnitRunner kukumoJUnitRunner;
    private final TestContextManager testContextManager;


    public KukumoSpringJUnitRunner(Class<?> configurationClass) throws InitializationError {
        /** IMPORTANT: TestContext must be prepared before accessing Kukumo */
        this.testContextManager = createTestContextManager(configurationClass);
        try {
            this.testContextManager.prepareTestInstance(this);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(e.getMessage(), e);
        }
        this.kukumoJUnitRunner = new KukumoJUnitRunner(configurationClass);
    }


    protected TestContextManager createTestContextManager(Class<?> clazz) {
        return new TestContextManager(clazz);
    }


    protected final TestContextManager getTestContextManager() {
        return this.testContextManager;
    }


    @Override
    public Description getDescription() {
        return kukumoJUnitRunner.getDescription();
    }


    @Override
    public void run(RunNotifier notifier) {
        kukumoJUnitRunner.run(notifier);
    }
}