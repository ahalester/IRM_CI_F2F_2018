package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5767 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5767Login.class);
    suite.addTestSuite(ICT5767TestCase01.class);
    suite.addTestSuite(ICT5767Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
