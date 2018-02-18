package org.lcristalli.commons.testing;

import junitparams.JUnitParamsRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple builder that can be used in combination with {@link JUnitParamsRunner}
 */
public final class ParametricTests {
    private final List<Object[]> testCases = new ArrayList<>();

    public static ParametricTests testCases() {
        return new ParametricTests();
    }

    private ParametricTests() {
        // empty constructor to hide the implicit one
    }

    public ParametricTests add(Object... parameters) {
        testCases.add(parameters);
        return this;
    }

    public Object[] build() {
        if (testCases.isEmpty()) {
            throw new IllegalArgumentException("There are no test cases");
        }

        return testCases.toArray();
    }
}
