package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6948TestCase01 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6948Login.class);
    suite.addTestSuite(ICT6948TestCase01.class);
    suite.addTestSuite(ICT6948Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
