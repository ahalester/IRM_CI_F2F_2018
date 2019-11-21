package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6257 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6257Login.class);
    suite.addTestSuite(ICT6257TestCase01.class);
    suite.addTestSuite(ICT6257TestCase02.class);
    suite.addTestSuite(ICT6257Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
