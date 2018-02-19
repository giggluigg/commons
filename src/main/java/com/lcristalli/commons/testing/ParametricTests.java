package com.lcristalli.commons.testing;

import junitparams.JUnitParamsRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple builder that can be used in combination with {@link JUnitParamsRunner}
 * <p>
 *
 * Example of usage:
 * <p>
 * {@code ParametricTests.testCases().add(parameter1, parameter2).build()}
 * <p>
 * It expects at least 1 test case, otherwise an exception is thrown. See {@link #build()}
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
