package com.lcristalli.commons;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class provides a fluent api to specify requirements in assertions and validations.
 * It helps building a {@link Predicate} to test on an object, throwing an exception if the test fails (i.e. returns {@code false}).
 *
 * Example of usage:
 *
 * {@code Requirements.require(object).toSatisfy(aJavaPredicateExpectedToBeTrue).otherwiseThrow(() -> new RuntimeException("Error")}
 *
 * {@link Predicate}s can be composed by using {@link RequirementBuilder.PredicateBuilder#and(Predicate)} and {@link RequirementBuilder.PredicateBuilder#or(Predicate)}
 *
 */
public final class Requirements {

    private Requirements() {
        //empty constructor to hide the implicit one
    }

    /**
     * Factory method for the builder. Start here to specify the requirements on the given instance.
     * This is the {@code Step 1} of the building process
     *
     * @param theObjectToTest   the object on which the resulting {@link Predicate} will be tested
     * @param <T>               the type of the object under test
     *
     * @return an instance of {@link RequirementBuilder} that will be used to specify the desired requirements on {@code theObjectToTest}
     */
    public static <T> RequirementBuilder<T> require(T theObjectToTest) {
        return new RequirementBuilder<>(theObjectToTest);
    }

    public static class RequirementBuilder<T> {
        private final T theObjectToTest;

        private RequirementBuilder(T theObjectToTest) {
            this.theObjectToTest = theObjectToTest;
        }

        /**
         * This method accepts a {@link Predicate} to test on the given object.
         * This is the {@code Step 2} of the building process
         *
         * @param predicate the aforementioned {@link Predicate}
         *
         * @return {@code this} {@link PredicateBuilder} to continue specifying the requirement
         */
        public PredicateBuilder toSatisfy(Predicate<T> predicate) {
            return new PredicateBuilder(predicate);
        }

        /**
         * Builder for the {@link Predicate} to test.
         *
         * The composition using the {@link #and(Predicate)} and {@link #or(Predicate)} does NOT consider any precedence
         * but it preserves the order used while building.
         *
         * This means that {@code A or B and C} is evaluated like {@code (A or B) and C} and NOT like {@code A or (B and C)}
         *
         * This behaviour is by design, for consistency with {@link Predicate#and(Predicate)} and {@link Predicate#or(Predicate)},
         * on whose implementations the code relies.
         *
         * Since it is easy to produce code difficult to read by creating complex and long combinations, it is recommended not to use
         * ambiguous builds and rely instead on the composition of the predicates themselves.
         * In fact, the idea of exposing these methods is only for those cases where they make the code more readable than
         * the one that could result by composing the {@link Predicate}s.
         */
        public class PredicateBuilder {
            private Predicate<T> predicate;

            private PredicateBuilder(Predicate<T> predicate) {
                this.predicate = predicate;
            }

            /**
             * Combines in logical {@code AND} the given {@code predicate} to the one being built.
             * NO precedence is applied.
             *
             * @param predicate the new {@link Predicate} to combine to the one being built
             *
             * @return {@code this} {@link PredicateBuilder} to continue specifying the requirement
             */
            public PredicateBuilder and(Predicate<T> predicate) {
                this.predicate = this.predicate.and(predicate);
                return this;
            }

            /**
             * Combines in logical {@code OR} the given {@code predicate} to the one being built.
             * NO precedence is applied.
             *
             * @param predicate the new {@link Predicate} to combine to the one being built
             *
             * @return {@code this} {@link PredicateBuilder} to continue specifying the requirement
             */
            public PredicateBuilder or(Predicate<T> predicate) {
                this.predicate = this.predicate.or(predicate);
                return this;
            }

            /**
             * Tests the built {@link Predicate} on the object for which it is required to be {@code true} and if
             * the result is {@code false}, the supplied {@link RuntimeException} is thrown
             *
             * This is the {@code Step 3} of the building process, the last one.
             *
             *
             * @param exception the {@link RuntimeException} that must be throw if the test on the object returns {@code false}
             */
            public void otherwiseThrow(Supplier<? extends RuntimeException> exception) {
                if (!predicate.test(theObjectToTest)) {
                    throw exception.get();
                }
            }
        }

    }

}
