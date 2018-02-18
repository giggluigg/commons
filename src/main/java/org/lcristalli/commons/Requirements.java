package org.lcristalli.commons;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class provides a fluent api to specify requirements in assertions and validations
 */
public final class Requirements {

    private Requirements() {
        //empty constructor to hide the implicit one
    }

    public static <T> RequirementBuilder<T> require(T object) {
        return new RequirementBuilder<>(object);
    }

    public static class RequirementBuilder<T> {
        private final T object;

        private RequirementBuilder(T object) {
            this.object = object;
        }

        public PredicateBuilder toSatisfy(Predicate<T> predicate) {
            return new PredicateBuilder(predicate);
        }

        public class PredicateBuilder {
            private Predicate<T> predicate;

            private PredicateBuilder(Predicate<T> predicate) {
                this.predicate = predicate;
            }

            public PredicateBuilder and(Predicate<T> predicate) {
                this.predicate = this.predicate.and(predicate);
                return this;
            }

            public PredicateBuilder or(Predicate<T> predicate) {
                this.predicate = this.predicate.or(predicate);
                return this;
            }

            public void otherwiseThrow(Supplier<? extends RuntimeException> exception) {
                if (!predicate.test(object)) {
                    throw exception.get();
                }
            }
        }

    }

}
