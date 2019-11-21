package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5878Login.class);
    suite.addTestSuite(ICT5878TestCase01.class);
    suite.addTestSuite(ICT5878Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
