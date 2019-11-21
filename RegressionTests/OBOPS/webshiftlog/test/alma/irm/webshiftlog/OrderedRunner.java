/*
 * NAME: OrderedRunner.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  Control the execution order. 
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/*
 * Control the order for testing
 */

public class OrderedRunner extends BlockJUnit4ClassRunner {
	public OrderedRunner(Class<?> klass) throws InitializationError{
		super(klass);
	}
	
	@Override
	protected List<FrameworkMethod> computeTestMethods(){
		List<FrameworkMethod> list = super.computeTestMethods();
		Collections.sort(list, new Comparator<FrameworkMethod>(){
			// The following annotation caused warning.
//			@Override
			public int compare(FrameworkMethod f1, FrameworkMethod f2){
				Order o1 = f1.getAnnotation(Order.class);
				Order o2 = f2.getAnnotation(Order.class);
				
				if(o1 == null || o2 == null){
					return -1;
				}
				return o1.order() - o2.order();
			}
		});
		return list;
	}
}
