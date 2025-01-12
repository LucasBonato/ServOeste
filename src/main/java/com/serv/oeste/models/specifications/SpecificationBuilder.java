package com.serv.oeste.models.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpecificationBuilder<T> {
    private Specification<T> specification;

    public SpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public SpecificationBuilder<T> add(Specification<T> specification) {
        if (specification != null) {
            this.specification = this.specification.and(specification);
        }
        return this;
    }

    public <V> SpecificationBuilder<T> addIfNotNull(V value, Function<V, Specification<T>> specificationFunction) {
        if (value != null) {
            this.specification = this.specification.and(specificationFunction.apply(value));
        }
        return this;
    }

    public <V> SpecificationBuilder<T> addIf(Predicate<V> condition, V value, Function<V, Specification<T>> specificationFunction) {
        if (value != null && condition.test(value)) {
            this.specification = this.specification.and(specificationFunction.apply(value));
        }
        return this;
    }

    public SpecificationBuilder<T> addDateRange(Date start, Date end, BiFunction<Date, Date, Specification<T>> rangeSpecification, Function<Date, Specification<T>> singleSpecification) {
        if (start != null && end != null) {
            this.specification = this.specification.and(rangeSpecification.apply(start, end));
        } else if (start != null) {
            this.specification = this.specification.and(singleSpecification.apply(start));
        }
        return this;
    }

    public Specification<T> build() {
        return this.specification;
    }
}
