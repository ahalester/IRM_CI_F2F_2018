package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6179 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6179Login.class);
    suite.addTestSuite(ICT6179TestCase01.class);
    suite.addTestSuite(ICT6179Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
