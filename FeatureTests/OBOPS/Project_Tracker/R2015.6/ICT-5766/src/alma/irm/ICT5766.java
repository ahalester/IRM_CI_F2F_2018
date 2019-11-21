package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5766 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5766Login.class);
    suite.addTestSuite(ICT5766TestCase01.class);
    suite.addTestSuite(ICT5766Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
