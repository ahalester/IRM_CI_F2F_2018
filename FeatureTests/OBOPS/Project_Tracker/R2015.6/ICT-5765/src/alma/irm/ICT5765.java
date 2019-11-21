package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5765 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5765Login.class);
    suite.addTestSuite(ICT5765TestCase01.class);
    suite.addTestSuite(ICT5765Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
