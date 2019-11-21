package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5410 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5410Login.class);
    suite.addTestSuite(ICT5410TestCase01.class);
    suite.addTestSuite(ICT5410TestCase02.class);
    suite.addTestSuite(ICT5410Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
