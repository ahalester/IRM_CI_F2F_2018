package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT7003 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT7003Login.class);
    suite.addTestSuite(ICT7003TestCase01.class);
    suite.addTestSuite(ICT7003Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
