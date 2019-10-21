package ar.com.tbf.common.data.accessibility;

import java.util.HashMap;
import java.util.Map;

import ar.com.tbf.common.data.predicate.SpecificPredicate;

public class AttributePredicateHelperAccessibility {

	private static ThreadLocal<AttributePredicateHelperCollection> attributePredicateHelperCollection = new ThreadLocal<AttributePredicateHelperCollection>() {
		
		protected AttributePredicateHelperCollection initialValue() {
			return new AttributePredicateHelperAccessibility().new AttributePredicateHelperCollection();
		}
	};
	
	public static boolean has( String attributeName ) {
		
		return attributePredicateHelperCollection.get().attributes.containsKey( attributeName );
	}
	
	public static void add( String attributeName, SpecificPredicate specificPredicate ) {
		
		if( ! attributePredicateHelperCollection.get().attributes.containsKey( attributeName ) ) {
			
			attributePredicateHelperCollection.get().attributes.put(attributeName, specificPredicate);
		}
	}
	
	public static SpecificPredicate get( String attributeName ) {
		
		return attributePredicateHelperCollection.get().attributes.get( attributeName );
	}
	
	public class AttributePredicateHelperCollection{
	
		public Map<String, SpecificPredicate> attributes = new HashMap<String, SpecificPredicate>();

	}
	
}
