package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5937 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5937Login.class);
    suite.addTestSuite(ICT5937TestCase01.class);
    suite.addTestSuite(ICT5937Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
