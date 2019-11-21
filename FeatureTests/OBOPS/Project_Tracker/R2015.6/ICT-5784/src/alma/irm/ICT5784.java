package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5784 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5784Login.class);
    suite.addTestSuite(ICT5784TestCase01.class);
    suite.addTestSuite(ICT5784Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
