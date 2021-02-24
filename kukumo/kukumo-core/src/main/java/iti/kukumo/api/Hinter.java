/**
 * @author Luis Iñesta Gelabert - luiinge@gmail.com
 */
package iti.kukumo.api;
import java.util.List;


/**
 * This interface provides utility methods to offer completion suggestions
 * about a test plan.
 */
public interface Hinter {

    List<String> getExpandedAvailableSteps();

    List<String> getCompactAvailableSteps();

    List<String> getAvailableProperties();

    boolean isValidStep(String step);

    List<String> getHintsForInvalidStep(
        String invalidStep,
        int numberOfHints,
        boolean includeVariations
    );

}
