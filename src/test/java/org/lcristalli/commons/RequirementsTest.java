package org.lcristalli.commons;

import junitparams.JUnitParamsRunner;
import junitparams.NamedParameters;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.rules.ExpectedException.none;
import static org.lcristalli.commons.Requirements.require;
import static org.lcristalli.commons.testing.ParametricTests.testCases;

/**
 * Unit tests for {@link Requirements}
 */
@RunWith(JUnitParamsRunner.class)
public class RequirementsTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void ifPredicateIsFalseOnObjectThenTheSuppliedExceptionIsThrown() {
        final RuntimeException thrownException = new RuntimeException("test");

        expectedException.expect(sameInstance(thrownException));

        require(new Object()).toSatisfy(Objects::isNull).otherwiseThrow(() -> thrownException);
    }

    @Test
    public void ifPredicateIsTrueOnObjectThenNoExceptionIsThrown() {
        require(new Object()).toSatisfy(Objects::nonNull).otherwiseThrow(RuntimeException::new);
    }

    @Test
    @Parameters(named = "testCasesForLogicalAnd")
    public void whenUsingTwoPredicatesInAndWithOneOrBothPredicatesEvaluatingToFalseThenTheSuppliedExceptionIsThrown(String testedString) throws Exception {
        final RuntimeException thrownException = new RuntimeException("test");

        expectedException.expect(sameInstance(thrownException));

        require(testedString).toSatisfy(s -> s.startsWith("a")).and(s -> s.endsWith("z")).otherwiseThrow(() -> thrownException);
    }

    @NamedParameters("testCasesForLogicalAnd")
    private Object[] testCasesForLogicalAnd() {
        return testCases()
                .add(" z")
                .add("a ")
                .add(" az ")
                .build();
    }

    @Test
    public void whenCombiningWithLogicalAndTwoPredicatesEvaluatingToTrueThenNoExceptionIsThrown() throws Exception {
        final String testedString = "passingTest";

        require(testedString).toSatisfy(s -> s.startsWith("p")).and("passingTest"::equals).otherwiseThrow(RuntimeException::new);
    }

    @Test
    @Parameters(named = "testCasesForLogicalOr")
    public void whenUsingTwoPredicatesInOrWithOneOrBothPredicatesEvaluatingToTrueThenNoExceptionIsThrown(String testedString) throws Exception {
        require(testedString).toSatisfy(s -> s.startsWith("a")).or(s -> s.endsWith("z")).otherwiseThrow(RuntimeException::new);
    }

    @NamedParameters("testCasesForLogicalOr")
    private Object[] testCasesForLogicalOr() {
        return testCases()
                .add("abcz")
                .add("ab ")
                .add("cz")
                .build();
    }

    @Test
    public void whenUsingTwoPredicatesInOrWithBothEvaluatingToFalseThenTheSuppliedExceptionIsThrown() throws Exception {
        final RuntimeException thrownException = new RuntimeException("test");

        expectedException.expect(sameInstance(thrownException));

        require("test").toSatisfy(s -> s.startsWith("a")).or(s -> s.startsWith("b")).otherwiseThrow(() -> thrownException);
    }

    @Test
    public void logicalConnectorsAreAppliedInTheOrderTheyAreUsed() throws Exception {
        Assert.fail();
    }
}