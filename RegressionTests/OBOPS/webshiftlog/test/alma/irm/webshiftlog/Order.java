/*
 * NAME: Order.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  Definition of the new annotation "Order". 
 *  
 * PRE- AND POST-CONDITIONS:
 *  
 * MAINTENANCE:
 *   
 * ----------------------------------------------------------------
 * AMENDEMENTS
 * <date>		<name>	<what & why>
 * 2016-06-10	knaka	Updated for R2015.8
 * 2017-03-09	knaka	Updated for FEB2017
 * 2017-04-17	knaka	Put light documentation on header
 * ----------------------------------------------------------------
 */
package alma.irm.webshiftlog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * Created a new annotation
 * This controls the order with test cases
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
	public int order();
}
