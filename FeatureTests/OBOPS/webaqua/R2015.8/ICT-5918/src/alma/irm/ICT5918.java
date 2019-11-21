package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5918 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5918Login.class);
    suite.addTestSuite(ICT5918TestCase01.class);
    suite.addTestSuite(ICT5918Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
