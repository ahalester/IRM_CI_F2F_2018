package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6018 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6018Login.class);
    suite.addTestSuite(ICT6018TestCase01.class);
    suite.addTestSuite(ICT6018Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
