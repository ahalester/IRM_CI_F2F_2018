package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5542 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5542Login.class);
    suite.addTestSuite(ICT5542TestCase01.class);
    suite.addTestSuite(ICT5542Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
