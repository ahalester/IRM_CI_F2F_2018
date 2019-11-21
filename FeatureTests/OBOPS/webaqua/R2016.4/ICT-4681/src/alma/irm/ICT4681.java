package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT4681 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT4681Login.class);
    suite.addTestSuite(ICT4681TestCase01.class);
    suite.addTestSuite(ICT4681Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
