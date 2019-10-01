package iti.kukumo.core.runner;

import iti.commons.configurer.Configuration;
import iti.kukumo.api.KukumoConfiguration;
import iti.kukumo.api.plan.NodeType;
import iti.kukumo.api.plan.PlanNode;
import iti.kukumo.api.plan.PlanNodeExecution;
import iti.kukumo.api.plan.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


/**
 * @author ITI
 * Created by ITI on 26/08/19
 */
public class PlanNodeLogger {

    private final boolean showStepSource;
    private final boolean showElapsedTime;
    private final Logger logger;

    private long totalNumberTestCases;
    private long currentTestCaseNumber;

    public PlanNodeLogger(Logger logger, Configuration configuration, PlanNode plan) {
        this.logger = logger;
        this.showStepSource = configuration
           .get(KukumoConfiguration.LOGS_SHOW_STEP_SOURCE,Boolean.class)
           .orElse(true);
        this.showElapsedTime = configuration
           .get(KukumoConfiguration.LOGS_SHOW_ELAPSED_TIME,Boolean.class)
           .orElse(true);
        this.totalNumberTestCases = plan.numDescendants(NodeType.TEST_CASE);
    }


    public void logTestPlanHeader(PlanNode plan) {
        if (logger.isInfoEnabled()) {
            int numTestCases = plan.numDescendants(NodeType.TEST_CASE);
            logger.info("{!important} Running Test Plan with {} Test Cases...", numTestCases);
        }
    }


    public void logTestPlanResult(PlanNode plan) {
        if (logger.isInfoEnabled()) {
            Result result = plan.result().orElse(Result.ERROR);
            int numTestCases = plan.numDescendants(NodeType.TEST_CASE);
            int numTestCasesPassed = plan.numDescendants(NodeType.TEST_CASE, Result.PASSED);
            String resultStyle = "stepResult."+plan.result().orElse(null);
            logger.info("{!"+resultStyle+"}=========================");
            logger.info("{!"+resultStyle+"}Test Plan {}"+(result.isPassed() ? "" : "  ({} of {} test cases not passed)"),
                result,
                numTestCases - numTestCasesPassed,
                numTestCases
            );
            logger.info("{!"+resultStyle+"}=========================");
        }
    }



    public void logTestCaseHeader(PlanNode node) {
        if (node.nodeType() != NodeType.TEST_CASE) {
            return;
        }
        currentTestCaseNumber++;
        if (logger.isInfoEnabled()) {
            StringJoiner name = new StringJoiner(" : ");
            if (node.keyword() != null) {
                name.add(node.keyword());
            }
            name.add(node.name());
            logger.info("{highlight}", StringUtils.repeat("-",name.length()+4));
            logger.info("{highlight} (Test Case {}/{})", "| "+name+" |", currentTestCaseNumber, totalNumberTestCases);
            logger.info("{highlight}", StringUtils.repeat("-",name.length()+4));
        }
    }

    public void logStepResult(PlanNode step) {
        if (step.nodeType() != NodeType.STEP) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(buildMessage(step),buildMessageArgs(step));
        }
        step.execution().flatMap(PlanNodeExecution::error).ifPresent(error->logger.debug("stack trace:", error));
    }


    private String buildMessage(PlanNode step) {
        String resultStyle = "stepResult."+step.result().orElse(null);
        StringBuilder message = new StringBuilder();
        message.append("{highlight} {"+resultStyle+"} {highlight}");
        if (showStepSource) {
            message.append("{source} :");
        }
        message.append(" {keyword} {}");
        if (showElapsedTime) {
            message.append(" {time} ");
        }
        message.append("{"+resultStyle+"}");
        return message.toString();
    }


    private Object[] buildMessageArgs(PlanNode step) {
        PlanNodeExecution execution = step.execution().orElse(null);
        List<Object> args = new ArrayList<>();
        args.add("[");
        args.add(execution.result().orElse(null));
        args.add("]");
        if (showStepSource) {
            args.add(step.source());
        }
        args.add(emptyIfNull(step.keyword()));
        args.add(step.name());
        if (showElapsedTime) {
            String duration = (
                    execution.result().orElse(null) == Result.SKIPPED ? "" :
                            "("+ (execution.duration().map(Duration::toMillis).orElse(0L) / 1000f) + ")"
            );
            args.add(duration);
        }
        args.add(execution.error().map(Throwable::getLocalizedMessage).orElse(""));
        return args.toArray();
    }



    private static Object emptyIfNull(Object value) {
        return value == null ? "" : value;
    }



}
