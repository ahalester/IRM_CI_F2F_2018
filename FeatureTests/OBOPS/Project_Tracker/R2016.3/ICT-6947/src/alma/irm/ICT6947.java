package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6947 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6947Login.class);
    suite.addTestSuite(ICT6947TestCase01.class);
    suite.addTestSuite(ICT6947Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
