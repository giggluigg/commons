package org.lcristalli.commons;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.rules.ExpectedException.none;
import static org.lcristalli.commons.Requirements.require;

/**
 * Unit tests for {@link Requirements}
 */
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
}