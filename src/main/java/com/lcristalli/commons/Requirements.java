package com.lcristalli.commons;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class provides a fluent api to specify requirements in assertions and validations.<br>
 * It helps building a {@link Predicate} to test on an object, throwing a {@link RuntimeException} if the test fails (i.e. returns {@code false}).
 * <p>
 * Typical usage will look like:
 * <pre>
 * {@code Requirements.require(object)
 *           .toSatisfy(aJavaPredicateExpectedToBeTrue)
 *           .otherwiseThrow(() -> new RuntimeException("Error")}
 * </pre>
 */
public final class Requirements {

    private Requirements() {
        //empty constructor to hide the implicit one
    }

    /**
     * Factory method for the builder. Start here to specify the requirements on the given instance.<br>
     * This is the {@code Step 1} of the building process
     *
     * @param theObjectToTest   the object on which the resulting {@link Predicate} will be tested
     * @param <T>               the type of the object under test
     *
     * @return An instance of {@link RequirementBuilder} that will be used to specify the desired requirements on {@code theObjectToTest}
     */
    public static <T> RequirementBuilder<T> require(T theObjectToTest) {
        return new RequirementBuilder<>(theObjectToTest);
    }

    public static final class RequirementBuilder<T> {
        private final T theObjectToTest;

        private RequirementBuilder(T theObjectToTest) {
            this.theObjectToTest = theObjectToTest;
        }

        /**
         * This method accepts a {@link Predicate} to test on the given object.<br>
         * This is the {@code Step 2} of the building process
         *
         * @param predicate the aforementioned {@link Predicate}
         *
         * @return An instance of {@link PredicateBuilder} to continue composing the {@link Predicate} as needed
         */
        public PredicateBuilder toSatisfy(Predicate<T> predicate) {
            return new PredicateBuilder(predicate);
        }

        /**
         * Builder for the {@link Predicate} to test.
         * <p>
         * The composition via the methods {@link #and(Predicate)} and {@link #or(Predicate)} does NOT consider any precedence
         * but it preserves the order used while building.<br>
         * This means that {@code (A or B and C)} is evaluated like {@code ((A or B) and C)} and NOT like {@code (A or (B and C))}<br>
         * This behaviour is by design, for consistency with {@link Predicate#and(Predicate)} and {@link Predicate#or(Predicate)},
         * on whose implementations the code also relies.
         * <p>
         * It is strongly recommended to always use the composition of {@link Predicate}s
         * unless the implementation provided here makes the code more readable.
         */
        public final class PredicateBuilder {
            private Predicate<T> predicate;

            private PredicateBuilder(Predicate<T> predicate) {
                this.predicate = predicate;
            }

            /**
             * Combines in logical {@code AND} the given {@code predicate} to the one being built.<br>
             * NO precedence is applied.
             *
             * @param predicate the new {@link Predicate} to combine to the one being built
             *
             * @return The same instance of {@link PredicateBuilder}
             */
            public PredicateBuilder and(Predicate<T> predicate) {
                this.predicate = this.predicate.and(predicate);
                return this;
            }

            /**
             * Combines in logical {@code OR} the given {@code predicate} to the one being built.<br>
             * NO precedence is applied.
             *
             * @param predicate the new {@link Predicate} to combine to the one being built
             *
             * @return The same instance of {@link PredicateBuilder}
             */
            public PredicateBuilder or(Predicate<T> predicate) {
                this.predicate = this.predicate.or(predicate);
                return this;
            }

            /**
             * Tests the built {@link Predicate} on the object for which it is required to be {@code true} and if
             * the result is {@code false}, the supplied {@link RuntimeException} is thrown.
             * <br>
             * This is the {@code Step 3} of the building process, the last one.
             *
             * @param exception A {@link Supplier} of the {@link RuntimeException} that must be thrown
             *                  if the test on the object returns {@code false}
             */
            public void otherwiseThrow(Supplier<? extends RuntimeException> exception) {
                if (!predicate.test(theObjectToTest)) {
                    throw exception.get();
                }
            }
        }

    }

}
