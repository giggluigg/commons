package org.lcristalli.commons.testing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.lcristalli.commons.testing.ParametricTests.testCases;

/**
 * Unit tests for {@link ParametricTests}
 */
public class ParametricTestsTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void buildReturnsAllTestsCasesPreviouslyAdded() {
        final Object[][] parametersByTestCases = {
                {new Object(), new Object()},
                {new Object(), new Object()},
        };

        final Object[] testCases = testCases()
                .add(parametersByTestCases[0][0], parametersByTestCases[0][1])
                .add(parametersByTestCases[1][0], parametersByTestCases[1][1])
                .build();

        assertThat(testCases, is(equalTo(parametersByTestCases)));
    }

    @Test
    public void buildingWithoutTestCasesThrowsRuntimeException() {
        expectedException.expect(IllegalArgumentException.class);

        testCases().build();
    }
}