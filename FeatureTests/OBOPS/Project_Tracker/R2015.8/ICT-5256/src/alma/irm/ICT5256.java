package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5256 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5256Login.class);
    suite.addTestSuite(ICT5256TestCase01.class);
    suite.addTestSuite(ICT5256TestCase02.class);
    suite.addTestSuite(ICT5256TestCase03.class);
    suite.addTestSuite(ICT5256Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
