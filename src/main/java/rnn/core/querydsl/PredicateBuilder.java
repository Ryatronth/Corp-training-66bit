package rnn.core.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.function.Function;

public class PredicateBuilder {

    private final BooleanBuilder predicateBuilder;

    private PredicateBuilder() {
        this.predicateBuilder = new BooleanBuilder();
    }

    public <T> PredicateBuilder and(T filterValue, Function<T, Predicate> predicateFunction) {
        if (filterValue != null) {
            predicateBuilder.and(predicateFunction.apply(filterValue));
        }
        return this;
    }

    public <T> PredicateBuilder or(T filterValue, Function<T, Predicate> predicateFunction) {
        if (filterValue != null) {
            predicateBuilder.or(predicateFunction.apply(filterValue));
        }
        return this;
    }

    public BooleanBuilder build() {
        return predicateBuilder;
    }

    public static PredicateBuilder builder() {
        return new PredicateBuilder();
    }
}
