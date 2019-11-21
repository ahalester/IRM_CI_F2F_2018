package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT7039 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT7039Login.class);
    suite.addTestSuite(ICT7039TestCase01.class);
    suite.addTestSuite(ICT7039Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
