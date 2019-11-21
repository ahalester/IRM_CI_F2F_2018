package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6945 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6945Login.class);
    suite.addTestSuite(ICT6945TestCase01.class);
    suite.addTestSuite(ICT6945Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
