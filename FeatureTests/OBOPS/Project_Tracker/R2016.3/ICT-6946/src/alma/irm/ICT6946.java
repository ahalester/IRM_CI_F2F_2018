package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6946 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6946Login.class);
    suite.addTestSuite(ICT6946TestCase01.class);
    suite.addTestSuite(ICT6946Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
