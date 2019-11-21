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
