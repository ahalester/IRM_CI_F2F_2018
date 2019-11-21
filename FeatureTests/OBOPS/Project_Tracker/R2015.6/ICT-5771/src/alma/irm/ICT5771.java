package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5771 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5771Login.class);
    suite.addTestSuite(ICT5771TestCase01.class);
    suite.addTestSuite(ICT5771Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
