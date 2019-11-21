package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6589 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6589Login.class);
    suite.addTestSuite(ICT6589TestCase01.class);
    suite.addTestSuite(ICT6589Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
