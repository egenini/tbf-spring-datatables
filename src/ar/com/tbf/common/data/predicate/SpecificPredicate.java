package ar.com.tbf.common.data.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public interface SpecificPredicate {

	public Predicate build( final From<?, ?> from, CriteriaBuilder builder, String key, Object value );
}
