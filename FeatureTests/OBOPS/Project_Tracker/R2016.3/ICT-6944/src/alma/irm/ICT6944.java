package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6944 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6944Login.class);
    suite.addTestSuite(ICT6944TestCase01.class);
    suite.addTestSuite(ICT6944Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
