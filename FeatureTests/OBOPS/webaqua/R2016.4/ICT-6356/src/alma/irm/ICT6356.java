package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6356 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6356Login.class);
    suite.addTestSuite(ICT6356TestCase01.class);
    suite.addTestSuite(ICT6356Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
