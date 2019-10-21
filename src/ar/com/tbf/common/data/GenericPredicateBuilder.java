package ar.com.tbf.common.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import ar.com.tbf.common.data.accessibility.AttributePredicateHelperAccessibility;

public class GenericPredicateBuilder {

	List<Predicate> predicates = null;
	CriteriaBuilder builder;
	
	public GenericPredicateBuilder withBuilder( CriteriaBuilder builder ) {
		
		this.builder = builder;
		
		return this;
	}
	
	/**
	 * Reemplaza la lista de predicados que tiene.
	 * 
	 * @param predicates
	 * @return
	 */
	public GenericPredicateBuilder withPredicates( List<Predicate> predicates ) {
		
		this.predicates = predicates;
		
		return this;
	}
	
	/**
	 * Crea la lista de predicados;
	 * 
	 * @return
	 */
	public GenericPredicateBuilder withPredicates() {
		
		this.predicates = new ArrayList<Predicate>();
		
		return this;
	}
	
	public GenericPredicateBuilder make( SearchOperation operation, From<?, ?> from, String key ,Object value ) {
		
		if( AttributePredicateHelperAccessibility.has( key ) ) {
			
			predicates.add( AttributePredicateHelperAccessibility.get( key ).build(from, builder, key, value )  );
		}
		else {
			
			switch ( operation ) {
			
			case EQUALITY:
				predicates.add( builder.equal(       from.get(key), value            ) );
				break;
			case NEGATION:
				predicates.add( builder.notEqual(    from.get(key), value            ) );
				break;
			case GREATER_THAN:
				predicates.add( builder.greaterThan( from.get(key), value.toString() ) );
				break;
			case LESS_THAN:
				predicates.add( builder.lessThan(    from.get(key), value.toString() ) );
				break;
			case LIKE:
				predicates.add( builder.like(        from.get(key), "%"+ value +"%"  ) );
				break;
			case STARTS_WITH:
				predicates.add( builder.like(        from.get(key), value +"%"       ) );
				break;
			case ENDS_WITH:
				predicates.add( builder.like(        from.get(key), "%"+ value       ) );
				break;
			case CONTAINS:
				predicates.add( builder.like(        from.get(key), "%"+ value +"%"  ) );
				break;
			default:
				break;
			}
		}
		
		return this;
	}
	
	public Predicate build() {
		
		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	public Predicate build(List<Predicate> predicates) {
		
		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
