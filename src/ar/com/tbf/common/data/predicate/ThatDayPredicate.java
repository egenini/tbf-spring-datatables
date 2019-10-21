package ar.com.tbf.common.data.predicate;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public class ThatDayPredicate implements SpecificPredicate{

	private DateFormat dateformat = null;
	
	public ThatDayPredicate( DateFormat dateformat ) {
		
		this.dateformat = dateformat;
	}
	
	public Predicate build( final From<?, ?> from, CriteriaBuilder builder, String key, Object value ) {
		
		Predicate predicate = null;
		
		try {
			Calendar init = Calendar.getInstance();
			
			init.setTime( dateformat.parse( (String) value ) );
			
			init.set( Calendar.HOUR       , 0);
			init.set( Calendar.MINUTE     , 0);
			init.set( Calendar.SECOND     , 0);
			init.set( Calendar.MILLISECOND, 0);
			
			Calendar end = Calendar.getInstance();
			
			end.setTime( init.getTime() );
			
			end.set( Calendar.HOUR       , 23);
			end.set( Calendar.MINUTE     , 59);
			end.set( Calendar.SECOND     , 59);
			end.set( Calendar.MILLISECOND, 999);
						
			predicate = builder.between( from.<Date>get(key), init.getTime(), end.getTime() );

		}catch( Exception e ) {
			e.printStackTrace();
		}
		
		return predicate;	
	}
}
