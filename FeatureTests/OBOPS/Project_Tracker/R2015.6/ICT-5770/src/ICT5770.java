package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5770 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5770Login.class);
    suite.addTestSuite(ICT5770TestCase01.class);
    suite.addTestSuite(ICT5770Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
