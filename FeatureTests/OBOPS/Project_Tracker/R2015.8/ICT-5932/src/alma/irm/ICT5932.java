package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5932 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5932Login.class);
    suite.addTestSuite(ICT5932TestCase01.class);
    suite.addTestSuite(ICT5932Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
