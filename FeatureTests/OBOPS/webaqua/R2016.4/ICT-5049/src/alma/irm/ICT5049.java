package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5049 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5049Login.class);
    suite.addTestSuite(ICT5049TestCase01.class);
    suite.addTestSuite(ICT5049Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
