package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5764 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5764Login.class);
    suite.addTestSuite(ICT5764TestCase01.class);
    suite.addTestSuite(ICT5764Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
