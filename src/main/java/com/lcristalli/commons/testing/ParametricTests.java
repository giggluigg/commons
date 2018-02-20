package com.lcristalli.commons.testing;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple builder that can be used in combination with JUnitParamsRunner.
 * <p>
 *
 * Example of usage:
 * <pre>
 * {@code ParametricTests.testCases()
 *           .add(parameter1ForCase1, parameter2ForCase1)
 *           .add(parameter1ForCase2, parameter2ForCase2)
 *           .build()}
 * </pre>
 * It expects at least 1 test case, otherwise an {@link IllegalArgumentException} is thrown.
 *
 * @see <a href="https://github.com/Pragmatists/JUnitParams">https://github.com/Pragmatists/JUnitParams</a>
 */
public final class ParametricTests {
    private final List<Object[]> testCases = new ArrayList<>();

    /**
     * Factory method for this builder
     *
     * @return an instance of {@link ParametricTests}
     */
    public static ParametricTests testCases() {
        return new ParametricTests();
    }

    private ParametricTests() {
        // empty constructor to hide the implicit one
    }

    /**
     * Adds the given {@code parameters} to {@code this} instance.
     *
     * @param parameters    the {@code parameters} used in the test case
     *
     * @return  The same instance of {@link ParametricTests} to continue adding test cases, if desired
     */
    public ParametricTests add(Object... parameters) {
        testCases.add(parameters);
        return this;
    }

    /**
     * Returns the array in which each item is an array with the parameters for a single test case
     *
     * @return  the array in which each item is an array with the parameters for a single test case
     */
    public Object[] build() {
        if (testCases.isEmpty()) {
            throw new IllegalArgumentException("There are no test cases");
        }

        return testCases.toArray();
    }
}
