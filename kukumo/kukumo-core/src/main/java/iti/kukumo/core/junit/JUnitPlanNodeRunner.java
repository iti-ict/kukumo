/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.core.junit;


import imconfig.Configuration;
import iti.kukumo.api.Backend;
import iti.kukumo.api.BackendFactory;
import iti.kukumo.api.KukumoException;
import iti.kukumo.api.KukumoSkippedException;
import iti.kukumo.api.plan.NodeType;
import iti.kukumo.api.plan.PlanNode;
import iti.kukumo.api.plan.Result;
import iti.kukumo.core.runner.PlanNodeLogger;
import iti.kukumo.core.runner.PlanNodeRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class JUnitPlanNodeRunner extends PlanNodeRunner {

    private Description description;
    private RunNotifier notifier;


    JUnitPlanNodeRunner(
        PlanNode node,
        Configuration configuration,
        BackendFactory backendFactory,
        Optional<Backend> backend,
        PlanNodeLogger logger
    ) {
        super(node, configuration, backendFactory, backend, logger);
    }


    JUnitPlanNodeRunner(
        PlanNode node,
        Configuration configuration,
        BackendFactory backendFactory,
        PlanNodeLogger logger
    ) {
        super(node, configuration, backendFactory, logger);
    }


    protected boolean isSuite() {
        return getNode().nodeType().isNoneOf(NodeType.TEST_CASE, NodeType.STEP);
    }


    public Description getDescription() {
        if (description == null) {
            if (isSuite()) {
                description = Description
                    .createSuiteDescription(getNode().displayName(), getUniqueId());
                for (PlanNodeRunner child : getChildren()) {
                    description.addChild(((JUnitPlanNodeRunner) child).getDescription());
                }
            } else {
                description = Description
                    .createTestDescription("", getNode().displayName(), getUniqueId());
            }
        }
        return description;
    }


    @Override
    protected List<PlanNodeRunner> createChildren() {
        return getNode().children()
            .map(
                child -> new JUnitPlanNodeRunner(
                    child, configuration(), backendFactory(), getBackend(), getLogger()
                )
            ).collect(Collectors.toList());
    }


    public void runNode(RunNotifier notifier) {
        this.notifier = notifier;
        super.runNode();
    }


    @Override
    protected void runChildren() {
        for (PlanNodeRunner child : getChildren()) {
            ((JUnitPlanNodeRunner) child).runNode(notifier);
        }
    }


    @Override
    protected void testCasePreExecution(PlanNode node) {
        super.testCasePreExecution(node);
        if (node.nodeType() == NodeType.TEST_CASE && getChildren().isEmpty()) {
            notifier.fireTestIgnored(getDescription());
        } else {
            notifier.fireTestStarted(getDescription());
        }
    }


    @Override
    protected void testCasePostExecution(PlanNode node) {
        super.testCasePostExecution(node);
        Exception notExecuted = new KukumoException(
            "Test case not executed due to unknown reasons"
        );
        Exception skipped = new KukumoSkippedException("Test case skipped");
        Optional<Result> result = node.result();
        if (result.isPresent()) {
            if (result.get() == Result.PASSED) {
                notifier.fireTestFinished(getDescription());
            } else if (result.get() == Result.SKIPPED) {
                notifier.fireTestFailure(new Failure(getDescription(), skipped));
            } else {
                Throwable error = node.errors().findFirst().orElse(notExecuted);
                if (error instanceof KukumoSkippedException) {
                    notifier.fireTestIgnored(getDescription());
                } else {
                    notifier.fireTestFailure(new Failure(getDescription(), error));
                }
            }
        }
    }

}