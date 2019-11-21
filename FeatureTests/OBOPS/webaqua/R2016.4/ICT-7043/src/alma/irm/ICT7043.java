package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT7043 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT7043Login.class);
    suite.addTestSuite(ICT7043TestCase01.class);
    suite.addTestSuite(ICT7043Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
